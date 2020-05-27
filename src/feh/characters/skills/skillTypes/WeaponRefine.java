package feh.characters.skills.skillTypes;

import java.net.URL;

public class WeaponRefine extends Weapon {
    private final String specialEff;
    private final URL iconURL;
    private final int[] refStatModifiers;
    private final int refCost, refMt, refRng;



    public WeaponRefine(String name, String description, String specialEff,
                        URL link, URL iconURL,
                        int[] refStatModifiers, int refCost, int refMt, int refRng) {
        super(name, description,
                link,
                refCost, true,
                refMt, refRng, null, null);
        this.specialEff = specialEff;
        this.iconURL = iconURL;
        this.refStatModifiers = refStatModifiers;
        this.refCost = refCost;
        this.refMt = refMt;
        this.refRng = refRng;
    }



    //public String getName() { return name; }
    public String getDescription() { return super.getDescription()+'\n'+getSpecialEff(); }
    /*public*/ private String getSpecialEff() { return specialEff; }
    public URL getIconURL() { return iconURL; }
    public int getRefCost() { return refCost; }
    public int getRefMt() { return refMt; }
    public int getRefRng() { return refRng; }
    public int[] getRefStatModifiers() {
        return refStatModifiers;
    }
}
