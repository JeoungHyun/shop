package Exception;

public class CartEmptyException extends RuntimeException {

	private String url;
	public CartEmptyException(String msg , String url) {
		super(msg);
		this.url=url;
	}
	public String getUrl() {
		return url;
	}
}
