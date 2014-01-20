package utilities;

//Offer type implementation
public class Offer {
	
	//seller's name
	private String _sellerName;
	//offer's id (seller's name + counter)
	private String _offerName;
	//offer's amount
	private Float _amount;
	
	//constructor
	public Offer() {
		_sellerName = "";
		_offerName = "";
		_amount = new Float(-1.);
	}
	
	//GETTERS/SETTERS//
	
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
	
	//to message string
	public String toACLMessage() {
		return (_sellerName + "~" + _offerName + "~" + _amount.toString());
	}
	
	//from message string (parser)
	public static Offer fromACLMessage(String message) {
		Offer offer = new Offer();
		String[] content = message.split("~");
		offer.setSellerName(content[0]);
		offer.setOfferName(content[1]);
		offer.setAmount(Float.parseFloat(content[2]));
		return offer;
	}
	
	//equals method (for contains and indexOf methods of List<T> class)
	public boolean equals(Object o) {
		Offer offer = (Offer) o;
		if(offer.getOfferName().equals(_offerName) && offer.getSellerName().equals(_sellerName))
			return true;
		else
			return false;
	}
}
