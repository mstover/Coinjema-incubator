package drafting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileConvert {

  /**
   *
   */
  public static void main(String[] args) {
    File dir = new File(args[0]);

    processDirectory(dir);

  }

  private static void processDirectory(File dir) {
    System.out.println(dir.getName());
    convertFiles(dir);
    //new FileRename(dir).rename();
    recurseDirectories(dir);
  }

  private static void recurseDirectories(File dir) {
    for (File f : dir.listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname.isDirectory();
      }
    })) {
      processDirectory(f);
    }

  }

  private static void convertFiles(File dir) {
    for (File f : dir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith("jpg") || name.toLowerCase().endsWith("jpeg") || name.toLowerCase().endsWith("png");
      }
    })) {
      try {
        processJPGFile(f);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

  }

  private static void processJPGFile(File f) throws IOException {
    File png = findPNGVersion(f);
    int[] pngImageGeometry = getImageGeometry(png);
    if (pngImageGeometry[1] != 1039 || pngImageGeometry[0] != 734) {
      int resolution = getResolution(f);
      System.out.println("Resolution = " + resolution);
      convertFile(f, resolution, png);
    }

  }

  private static void convertFile(File f, int resolution, File dest) {
    if (resolution == 0) {
      System.out.println("Could not find resolution for file "
          + f.getAbsolutePath());
    } else {
      if (resolution <= 300) {
        String[] buf = new String[]{
            "convert",
            f.getName(),
            "-filter",
            "Blackman",
            "-units",
            "PixelsPerInch",
            "-resample",
            "300x300",
            "-resize",
            "734x1039!",
            (dest != null) ? dest.getName() : stripExtension(f.getName()) + ".png"
        };
        try {
          System.out.println(getOutput(buf, f
              .getParentFile()));
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        double factor = (double) resolution / 300D;
        int newSize = (int) (factor * 1039);
        int newWidth = (int) (factor * 734);
        List<String> buf = new ArrayList();
        buf.add("convert");
        buf.add(f.getName());
        buf.add("-resize");
        buf.add(newWidth + "x" + newSize + "!");
        buf.add("-resample");
        buf.add("300x300");
        buf.add("-units");
        buf.add("PixelsPerInch");
        buf.add("-resize");
        buf.add("734x1039!");
        buf.add(f.getName() + ".png");
        try {
          System.out.println(getOutput((String[]) buf.toArray(), f
              .getParentFile()));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

  }

  private static String stripExtension(String name) {
    int i = name.lastIndexOf(".");
    if (i > -1) {
      return name.substring(0, i);
    } else {
      return name;
    }
  }

  private static int[] getImageGeometry(File png) {
    if (png == null) {
      return new int[]{0, 0};
    }
    try {
      for (String s : getOutput(new String[]{"identify", "-format", "%g", png.getName()},
          png.getParentFile())) {
        if (s != null) {
          s = s.trim();
          System.out.println("output = " + s);
          boolean index = s.matches("\\d+x\\d+\\+\\d+\\+\\d+");
          if (index) {
            int x = s.indexOf("x");
            int end = s.indexOf("+");
            return new int[]{
                Integer.parseInt(s.substring(0, x).trim()),
                Integer.parseInt(s.substring(x + 1, end))};
          }
        }
      }
    } catch (NumberFormatException e) {
      return new int[]{0, 0};
    } catch (IOException e) {
      return new int[]{0, 0};
    }
    return new int[]{0, 0};
  }

  private static File findPNGVersion(File f) {
    File parent = f.getParentFile();
    final String cardName = f.getName().substring(0,
        f.getName().indexOf("."));
    String[] pngs = parent.list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".png") && name.startsWith(cardName + ".");
      }
    });
    if (pngs.length == 1) {
      return new File(parent, pngs[0]);
    } else {
      return null;
    }
  }

  private static int getResolution(File f) throws IOException {
    String[] cmd = new String[]{"identify", "-format", "%y", f.getName()};
    for (String s : getOutput(cmd, f.getParentFile())) {
      if (s != null) {
        System.out.println("Getting resolution: " + s);
        int index = s.indexOf("PixelsPer");
        if (index == -1) {
          index = s.indexOf("Undefined");
          try {
            int res = Integer.parseInt(s);
            return res;
          } catch (NumberFormatException e) {
            //noop
          }
        }
        if (index > -1) {
          String num = s.substring(0, index).trim();
          if (num.indexOf(".") > -1) {
            num = num.substring(0, num.indexOf("."));
          }
          int res = Integer.parseInt(num);
          String unit = s.substring(index).trim();
          if ("PixelsPerCentimeter".equals(unit)) {
            return (int) (res * 2.54D);
          } else {
            return res;
          }
        }
      }
    }
    return 0;
  }

  private static List<String> getOutput(String[] command, File dir)
      throws IOException {
    ArrayList<String> output = new ArrayList<String>();
    Process p = Runtime.getRuntime().exec(command, null, dir);

    BufferedReader read = new BufferedReader(new InputStreamReader(p
        .getErrorStream()));
    BufferedReader out = new BufferedReader(new InputStreamReader(p
        .getInputStream()));
    String outLine = out.readLine();
    while (outLine != null && outLine.length() > 0) {
      output.add(outLine);
      outLine = out.readLine();
    }
    out.close();
    String line = read.readLine();
    while (line != null && line.length() > 0) {
      output.add(line);
      line = read.readLine();
    }
    read.close();

    return output;
  }

}
