package elements;

public class ElementWasteGateau extends ElementWaste{

	protected ElementWasteGateau(Element elem) {
		super(elem);
		this.setBasicName("GāteauDechet()");
		this.setMinWeightCompost(0);
		
		this.setCompostable(true);
		this.setEdible(true);
		this.setFedable(true);
		this.setThrowable(true);
		this.setUsableForOther(true);
	}

}
