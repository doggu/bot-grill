package feh.heroes.character.constructionSite;

import jdk.dynalink.beans.MissingMemberHandlerFactory;

class Merger<T> {
    private final T a, b;



    Merger(T a, T b) {
        this.a = a;
        this.b = b;
    }



    T merge() throws MismatchedInputException /*, NullInputException*/ {
        if (true) {
            return (a==null)?b:a;
        }
        if (a==null) {
            if (b==null)
                return null; //throw new NullInputException();
            else
                return b;
        } else
            if (a instanceof String && b instanceof String) {
                if (((String) a).equals((String) b))
                    return a;
                else
                    throw new MismatchedInputException("1 \""+a+"\" != \""+b+"\"");
            } else if (b==null||a.equals(b))
                return a;
            else
                throw new MismatchedInputException("2 \""+a+"\" != \""+b+"\"");

    }
}
