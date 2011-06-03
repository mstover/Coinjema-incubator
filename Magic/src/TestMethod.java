import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author mstover
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TestMethod {

	String name();

}
