package strategiclibrary.service.classfinder;

/*
 * This file originally from The Apache project - JMeter.  Extensively modified by Global Crossing.
 * 
 * =============== original license ================================
 * Copyright 2000-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================
 */



import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

/**
 * This class finds classes that implement one or more specified interfaces.
 * 
 * @author <a href="mailto:mstover@glbx.net">Michael Stover </a>
 * @version $Revision: 1.1 $
 */
@CoinjemaObject(type="classfinder")
public class ClassFinder {
	
	Logger log;

	/**
	 * Convenience method for <code>findClassesThatExtend(Class[],
	 * boolean)</code>
	 * with the option to include inner classes in the search set to false.
	 * 
	 * @return ArrayList containing discovered classes.
	 */
	public List findClassesThatExtend(Class[] superClasses,
			List<String> paths) throws IOException, ClassNotFoundException {
		log.error("looking for classes: " + superClasses[0] + " on paths: " + paths);
		return findClassesThatExtend(superClasses, paths, false);
	}

	public List findClassesThatExtend(Class[] superClasses,
            List<String> paths, boolean innerClasses) throws IOException,
			ClassNotFoundException {
		log.info("looking for classes: " + superClasses[0] + " on paths: " + paths);
		List listPaths = null;
		ArrayList listClasses = null;
		List listSuperClasses = null;
		String[] strPathsOrJars = addJarsInPath(paths);
		listPaths = getClasspathMatches(strPathsOrJars);
		listClasses = new ArrayList();
		listSuperClasses = new ArrayList();
		for (int i = 0; i < superClasses.length; i++) {
			listSuperClasses.add(superClasses[i].getName());
		}
		log.info("matching paths = " + listPaths);
		// first get all the classes
		findClassesInPaths(listPaths, listClasses);
		List subClassList = findAllSubclasses(listSuperClasses, listClasses,
				innerClasses);
		return subClassList;
	}

	/**
	 * Find classes in the provided path(s)/jar(s) that extend the class(es).
	 * 
	 * @return ArrayList containing discovered classes
	 */
	private String[] addJarsInPath(List<String> paths) {
		Set fullList = new HashSet();
		for (String path : paths) {
			fullList.add(path);
			if (!path.endsWith(".jar")) {
				File dir = new File(path);
				if (dir.exists() && dir.isDirectory()) {
					String[] jars = dir.list(new FilenameFilter() {
						public boolean accept(File f, String name) {
							if (name.endsWith(".jar")) {
								return true;
							}
							return false;
						}
					});
					for (int x = 0; x < jars.length; x++) {
						fullList.add(jars[x]);
					}
				}
			}
		}
		return (String[]) fullList.toArray(new String[0]);
	}

	private List getClasspathMatches(String[] strPathsOrJars) {
		List listPaths = null;
		StringTokenizer stPaths = null;
		String strPath = null;
		int i;
		listPaths = new ArrayList();
		stPaths = new StringTokenizer(System.getProperty("java.class.path"),
				System.getProperty("path.separator"));
		if (strPathsOrJars != null) {
			strPathsOrJars = fixDotDirs(strPathsOrJars);
			strPathsOrJars = fixSlashes(strPathsOrJars);
			strPathsOrJars = fixEndingSlashes(strPathsOrJars);
		}
		// find all jar files or paths that end with strPathOrJar
		Set usedPaths = new HashSet();
		while (stPaths.hasMoreTokens()) {
			strPath = fixDotDir((String) stPaths.nextToken());
			strPath = fixSlashes(strPath);
			strPath = fixEndingSlashes(strPath);
			if (strPathsOrJars == null) {
				listPaths.add(strPath);
			} else {
				for (i = 0; i < strPathsOrJars.length; i++) {
					if (strPath.endsWith(strPathsOrJars[i])) {
						listPaths.add(strPath);
						usedPaths.add(strPathsOrJars[i]);
					}
				}
			}
		}
		//Add in valid paths from search path that weren't in the system
		// classpath variable
		for (i = 0; i < strPathsOrJars.length; i++) {
			if (!usedPaths.contains(strPathsOrJars[i])) {
				File path = new File(strPathsOrJars[i]);
				if (path.exists()) {
					listPaths.add(0, strPathsOrJars[i]);
				}
			}
		}
		return listPaths;
	}

	/**
	 * Get all interfaces that the class implements, including parent
	 * interfaces. This keeps us from having to instantiate and check
	 * instanceof, which wouldn't work anyway since instanceof requires a
	 * hard-coded class or interface name.
	 * 
	 * @param theClass
	 *            the class to get interfaces for
	 * @param hInterfaces
	 *            a Map to store the discovered interfaces in
	 */
	private void getAllInterfaces(Class theClass, Map hInterfaces) {
		Class[] interfaces = theClass.getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			hInterfaces.put(interfaces[i].getName(), interfaces[i]);
			getAllInterfaces(interfaces[i], hInterfaces);
		}
	}

	private String[] fixDotDirs(String[] paths) {
		for (int i = 0; i < paths.length; i++) {
			paths[i] = fixDotDir(paths[i]);
		}
		return paths;
	}

	private String fixDotDir(String path) {
		if (path != null && path.equals(".")) {
			return System.getProperty("user.dir");
		} else {
			return path.trim();
		}
	}

	private String[] fixEndingSlashes(String[] strings) {
		String[] strNew = new String[strings.length];
		for (int i = 0; i < strings.length; i++) {
			strNew[i] = fixEndingSlashes(strings[i]);
		}
		return strNew;
	}

	private String fixEndingSlashes(String string) {
		if (string.endsWith("/") || string.endsWith("\\")) {
			string = string.substring(0, string.length() - 1);
			string = fixEndingSlashes(string);
		}
		return string;
	}

	private String[] fixSlashes(String[] strings) {
		String[] strNew = new String[strings.length];
		for (int i = 0; i < strings.length; i++) {
			strNew[i] = fixSlashes(strings[i]) /* .toLowerCase() */;
		}
		return strNew;
	}

	private  String fixSlashes(String str) {
		// replace \ with /
		str = str.replace('\\', '/');
		// compress multiples into singles;
		// do in 2 steps with dummy string
		// to avoid infinte loop
		str = replaceString(str, "//", "_____");
		str = replaceString(str, "_____", "/");
		return str;
	}

	private  String replaceString(String s, String strToFind,
			String strToReplace) {
		int index;
		int currentPos;
		StringBuffer buffer = null;
		if (s.indexOf(strToFind) == -1) {
			return s;
		}
		currentPos = 0;
		buffer = new StringBuffer();
		while (true) {
			index = s.indexOf(strToFind, currentPos);
			if (index == -1) {
				break;
			}
			buffer.append(s.substring(currentPos, index));
			buffer.append(strToReplace);
			currentPos = index + strToFind.length();
		}
		buffer.append(s.substring(currentPos));
		return buffer.toString();
	}

	/**
	 * Determine if the class implements the interface.
	 * 
	 * @param theClass
	 *            the class to check
	 * @param theInterface
	 *            the interface to look for
	 * @return boolean true if it implements
	 */
	private  boolean classImplementsInterface(Class theClass,
			Class theInterface) {
		HashMap mapInterfaces = new HashMap();
		String strKey = null;
		// pass in the map by reference since the method is recursive
		getAllInterfaces(theClass, mapInterfaces);
		Iterator iterInterfaces = mapInterfaces.keySet().iterator();
		while (iterInterfaces.hasNext()) {
			strKey = (String) iterInterfaces.next();
			if (mapInterfaces.get(strKey) == theInterface) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Convenience method for <code>findAllSubclasses(List, List,
	 * boolean)</code>
	 * with the option to include inner classes in the search set to false.
	 * 
	 * @param listSuperClasses
	 *            the base classes to find subclasses for
	 * @param listAllClasses
	 *            the collection of classes to search in
	 * @return ArrayList of the subclasses
	 */
	private  ArrayList findAllSubclasses(List listSuperClasses,
			List listAllClasses) {
		return findAllSubclasses(listSuperClasses, listAllClasses, false);
	}

	/**
	 * Finds all classes that extend the classes in the listSuperClasses
	 * ArrayList, searching in the listAllClasses ArrayList.
	 * 
	 * @param listSuperClasses
	 *            the base classes to find subclasses for
	 * @param listAllClasses
	 *            the collection of classes to search in
	 * @param innerClasses
	 *            indicate whether to include inner classes in the search
	 * @return ArrayList of the subclasses
	 */
	private  ArrayList findAllSubclasses(List listSuperClasses,
			List listAllClasses, boolean innerClasses) {
		Iterator iterClasses = null;
		ArrayList listSubClasses = null;
		String strClassName = null;
		Class tempClass = null;
		listSubClasses = new ArrayList();
		iterClasses = listSuperClasses.iterator();
		while (iterClasses.hasNext()) {
			strClassName = (String) iterClasses.next();
			// only check classes if they are not inner classes
			// or we intend to check for inner classes
			if ((strClassName.indexOf("$") == -1) || innerClasses) {
				// might throw an exception, assume this is ignorable
				try {
					tempClass = Class.forName(strClassName, false, Thread
							.currentThread().getContextClassLoader());
					findAllSubclassesOneClass(tempClass, listAllClasses,
							listSubClasses, innerClasses);
					// call by reference - recursive
				} catch (Throwable ignored) {
				}
			}
		}
		return listSubClasses;
	}

	/**
	 * Convenience method for <code>findAllSubclassesOneClass(Class, List, List,
	 * boolean)</code>
	 * with option to include inner classes in the search set to false.
	 * 
	 * @param theClass
	 *            the parent class
	 * @param listAllClasses
	 *            the collection of classes to search in
	 * @param listSubClasses
	 *            the collection of discovered subclasses
	 */
	private  void findAllSubclassesOneClass(Class theClass,
			List listAllClasses, List listSubClasses) {
		findAllSubclassesOneClass(theClass, listAllClasses, listSubClasses,
				false);
	}

	/**
	 * Finds all classes that extend the class, searching in the listAllClasses
	 * ArrayList.
	 * 
	 * @param theClass
	 *            the parent class
	 * @param listAllClasses
	 *            the collection of classes to search in
	 * @param listSubClasses
	 *            the collection of discovered subclasses
	 * @param innerClasses
	 *            indicates whether inners classes should be included in the
	 *            search
	 */
	private  void findAllSubclassesOneClass(Class theClass,
			List listAllClasses, List listSubClasses, boolean innerClasses) {
		Iterator iterClasses = null;
		String strClassName = null;
		String strSuperClassName = null;
		Class c = null;
		Class cParent = null;
		boolean bIsSubclass = false;
		strSuperClassName = theClass.getName();
		iterClasses = listAllClasses.iterator();
		while (iterClasses.hasNext()) {
			strClassName = (String) iterClasses.next();
			// only check classes if they are not inner classes
			// or we intend to check for inner classes
			if ((strClassName.indexOf("$") == -1) || innerClasses) {
				// might throw an exception, assume this is ignorable
				try {
					// Class.forName() doesn't like nulls
					if (strClassName == null)
						continue;
					if (strClassName.endsWith(".groovy")) {
						GroovyClassLoader loader = new GroovyClassLoader(Thread.currentThread()
								.getContextClassLoader());
						try {
							c = loader.parseClass(loader.getResourceAsStream(strClassName));
						} catch (Throwable e) {
							try
							{
								File gFile = new File(strClassName);
								c = loader.parseClass(new FileInputStream(gFile));
							}
							catch(Throwable e1)
							{
								System.out.println("file load failed: " + e1);
							}
						}
					} else {
						c = Class.forName(strClassName, false, Thread
								.currentThread().getContextClassLoader());
					}

					if (!c.isInterface()
							&& !Modifier.isAbstract(c.getModifiers())) {
						bIsSubclass = theClass.isAssignableFrom(c);
					} else {
						bIsSubclass = false;
					}
					if (bIsSubclass) {
						listSubClasses.add(c);
					}
				} catch (Throwable ignored) {
				}
			}
		}
	}

	/**
	 * Converts a class file from the text stored in a Jar file to a version
	 * that can be used in Class.forName().
	 * 
	 * @param strClassName
	 *            the class name from a Jar file
	 * @return String the Java-style dotted version of the name
	 */
	private  String fixClassName(String strClassName) {
		strClassName = strClassName.replace('\\', '.');
		strClassName = strClassName.replace('/', '.');
		strClassName = strClassName.substring(0, strClassName.length() - 6);
		// remove ".class"
		return strClassName;
	}

	private  void findClassesInOnePath(String strPath, List listClasses)
			throws IOException {
		File file = null;
		String strPathName = null;
		ZipFile zipFile = null;
		ZipEntry zipEntry = null;
		Enumeration entries = null;
		String strEntry = null;
		file = new File(strPath);
		if (file.isDirectory()) {
			findClassesInPathsDir(strPath, file, listClasses);
		} else if (file.exists()) {
			zipFile = new ZipFile(file);
			entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				strEntry = entries.nextElement().toString();
				if (strEntry.endsWith(".class")) {
					listClasses.add(fixClassName(strEntry));
				} else if (strEntry.endsWith(".groovy")) {
					listClasses.add(strEntry);
				}
			}
		}
	}

	private  void findClassesInPaths(List listPaths, List listClasses)
			throws IOException {
		Iterator iterPaths = listPaths.iterator();
		while (iterPaths.hasNext()) {
			String path = (String) iterPaths.next();
			findClassesInOnePath(path, listClasses);
		}
	}

	private  void findClassesInPathsDir(String strPathElement, File dir,
			List listClasses) throws IOException {
		File file = null;
		String[] list = dir.list();
		for (int i = 0; i < list.length; i++) {
			file = new File(dir, list[i]);
			if (file.isDirectory()) {
				findClassesInPathsDir(strPathElement, file, listClasses);
			} else if (file.exists() && (file.length() != 0)
					&& list[i].endsWith(".class")) {
				listClasses.add(file.getPath().substring(
						strPathElement.length() + 1,
						file.getPath().lastIndexOf(".")).replace(
						File.separator.charAt(0), '.'));
			} else if (file.exists() && file.getName().endsWith(".groovy")) {
				listClasses.add(file.getPath());
			}
		}
	}

	@CoinjemaDependency(alias="log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

}