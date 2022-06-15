package com.rock.golf.Input;

import org.mdkt.compiler.InMemoryJavaCompiler;
import com.rock.golf.Physics.Engine.Der;

public class Crafter {
    public static Der initializeClass(){
        StringBuilder source = new StringBuilder()
        .append("package com.rock.golf.Input;\n")
        .append("import com.rock.golf.Physics.Engine.Der;\n")
        .append("public class Derivative implements Der{\n")
        .append("   private double h;")
        .append("   public Derivative(){\n")
        .append("       h = 0.0000001;\n")
        .append("   }\n")
        .append("   @Override\n")
        .append("    public double compute(double x, double y){\n")
        .append("       return " + InputModule.getProfileString() + ";\n")
        .append("   }\n")
        .append("   @Override\n")
        .append("   public double derivativeX(double x, double y){\n")
        .append("       return (compute(x+h, y) - compute(x-h, y)) / (2*h);\n")
        .append("   }\n")
        .append("   public double derivativeY(double x, double y){\n")
        .append("       return (compute(x, y+h) - compute(x, y-h)) / (2*h);\n")
        .append("   }\n")
        .append("}");
        InMemoryJavaCompiler comp = InMemoryJavaCompiler.newInstance();
        Class<?> derivationClass = null;
        Object a  = null;
        try {
            derivationClass = comp.compile("com.rock.golf.Input.Derivative", source.toString());
            a = derivationClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Der) a;
    }
}
