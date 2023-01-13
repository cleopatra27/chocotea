package com.chocotea.utility;

import com.chocotea.core.annotations.SpringRequest;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

public class GetIClass {
    public static Class getClass(TypeElement element) {
        try {/*w w w .j  a va 2s .  c  om*/
            return Class.forName(getClassName(element));
        } catch (Exception e) {
            //System.out.println(e);
        }
        return null;
    }


    public static Class getClass(TypeMirror type) {
        if (type instanceof DeclaredType) {
            if (((DeclaredType) type).asElement() instanceof TypeElement) {
                return getClass((TypeElement) ((DeclaredType) type)
                        .asElement());
            }
        }
        return null;
    }

    public static String getClassName(TypeElement element) {
        Element currElement = element;
        String result = element.getSimpleName().toString();
        while (currElement.getEnclosingElement() != null) {
            currElement = currElement.getEnclosingElement();
            if (currElement instanceof TypeElement) {
                result = currElement.getSimpleName() + "$" + result;
            } else if (currElement instanceof PackageElement) {
                if (!"".equals(currElement.getSimpleName())) {
                    result = ((PackageElement) currElement)
                            .getQualifiedName() + "." + result;
                }
            }
        }
        return result;
    }
}
