package feh.heroes.character.constructionSite;

class Merger<T> {
    private final T a, b;



    Merger(T a, T b) {
        this.a = a;
        this.b = b;
    }



    T merge() throws MismatchedInputException /*, NullInputException*/ {
        //if (true) { return (a==null)?b:a; }
        if (a==null)
            if (b==null)
                return null; //throw new NullInputException();
            else
                return b;
        else
            if (b != null)
                if (a.equals(b))
                    return a;
                else
                    throw new MismatchedInputException("\"" + a + "\" != \"" + b + "\"");
            else
                return a;
    }
}
