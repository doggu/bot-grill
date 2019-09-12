package feh.heroes.skills.skillTypes.constructionSite;

public class IncompleteDataException extends Exception {
    IncompleteDataException(String field) {
        super(field);
    }
}
