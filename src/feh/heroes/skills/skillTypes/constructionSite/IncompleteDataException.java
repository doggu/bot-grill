package feh.heroes.skills.skillTypes.constructionSite;

class IncompleteDataException extends Exception {
    IncompleteDataException(String field) {
        super(field);
    }
}
