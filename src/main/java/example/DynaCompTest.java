package example;

import java.io.BufferedReader;
import java.io.FileReader;

import example.dynamic.DynamicEngine;

public class DynaCompTest {
	public static void main(String[] args) throws Exception {
//        String fullName = "DynaClass";
        StringBuilder src = new StringBuilder();
//        src.append("public class DynaClass {\n");
//        src.append("    public String toString() {\n");
//        src.append("        return \"Hello, I am \" + ");
//        src.append("this.getClass().getSimpleName();\n");
//        src.append("    }\n");
//        src.append("}\n");
        BufferedReader reader = new BufferedReader( new FileReader(System.getProperty("user.dir")+"/src/main/java/example/script/Foo.java"));
        String temp = "";
        while((temp = reader.readLine()) != null){
        	src.append(temp).append("\n");
        }
        reader.close();
        System.out.println(src);
        DynamicEngine de = DynamicEngine.getInstance();
        Object instance =  de.javaCodeToObject("main.java.example.script.Foo",src.toString());
        System.out.println(instance);
    }
}
