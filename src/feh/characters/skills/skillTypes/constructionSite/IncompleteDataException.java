package feh.characters.skills.skillTypes.constructionSite;

public class IncompleteDataException extends Exception {
    IncompleteDataException(String field) {
        super(field);
    }
}
