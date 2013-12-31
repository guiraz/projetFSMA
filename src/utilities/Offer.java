package utilities;

public class Offer {
	
	private String _sellerName;
	private String _offerName;
	private Float _amount;
	
	public Offer() {
		_sellerName = "";
		_offerName = "";
		_amount = new Float(-1.);
	}
	
	public String getSellerName() {
		return _sellerName;
	}
	
	public void setSellerName(String seller) {
		_sellerName = seller;
	}
	
	public String getOfferName() {
		return _offerName;
	}
	
	public void setOfferName(String offerName) {
		_offerName = offerName;
	}
	
	public Float getAmount() {
		return _amount;
	}
	
	public void setAmount(Float amount) {
		_amount = amount;
	}
	
	public String toACLMessage() {
		return (_sellerName + "~" + _offerName + "~" + _amount.toString());
	}
	
	public static Offer fromACLMessage(String message) {
		Offer offer = new Offer();
		String[] content = message.split("~");
		offer.setSellerName(content[0]);
		offer.setOfferName(content[1]);
		offer.setAmount(Float.parseFloat(content[2]));
		return offer;
	}
	
	public boolean equals(Object o) {
		Offer offer = (Offer) o;
		if(offer.getOfferName().equals(_offerName) && offer.getSellerName().equals(_sellerName))
			return true;
		else
			return false;
	}
}
