package logic;

import java.io.File;
import java.net.http.HttpRequest;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import dao.BoardDao;
import dao.ItemDao;
import dao.SaleDao;
import dao.SaleItemDao;
import dao.UserDao;

@Service  //@Component + service(Controller 와 dao 중간 역할)
public class ShopService {
	@Autowired
	private ItemDao itemDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private SaleDao saleDao;
	@Autowired
	private SaleItemDao saleItemDao;
	@Autowired
	private BoardDao boardDao;
	
	public List<Item> getItemList() {
		return itemDao.list();
	}

	public Item getItem(Integer id) {
		return itemDao.selectOne(id);
		
	}

	public void itemCreate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			uploadFileCreate(item.getPicture(),request,"item/img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.insert(item);
	}

	private void uploadFileCreate(MultipartFile picture, HttpServletRequest request, String path) {
		//picture : 파일의 내용 저장
		String orgFile = picture.getOriginalFilename();
		String uploadPath=request.getServletContext().getRealPath("/")+path;
		File fpath= new File(uploadPath);
		if(!fpath.exists()) fpath.mkdirs();
		try {
			//파일의 내용을 실제 파일로 저장
			picture.transferTo(new File(uploadPath + orgFile));
		}catch(Exception e ){
			e.printStackTrace();
			
		}
		
	}

	public void itemUpdate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			uploadFileCreate(item.getPicture(),request,"item/img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.update(item);
		
	}

	public void itemdelete(Item item, HttpServletRequest request) {
		itemDao.delete(item);
		
	}

	public void itemdelete2(String id) {
		itemDao.delete2(id);
		
	}

	public void userInsert(User user) {
		
		userDao.insert(user);
		
	}

	public User getUser(String userid) {
	
		return userDao.selectOne(userid);
	}

	/*
	 * db에 sale 정보와 saleitem 정보 저장. 저장된 내용을 Sale 객체로 리턴
	 * 1. sale 테이블의 saleid 값을 설정 -> 최대값 + 1
	 * 2. sale의 내용 설정. -> insert
	 * 3. Cart 정보로부터 SaleItem 내용 설정
	 * 4. 모든 정보를 Sale 객체로 저장
	 * */
	public Sale checkend(User loginUser, Cart cart) { 
		ModelAndView mav = new ModelAndView();
		Sale sale = new Sale();
		int maxno = saleDao.getMaxSaleid();
		sale.setSaleid(++maxno);
		sale.setUser(loginUser);
		sale.setUserid(loginUser.getUserid());
		saleDao.insert(sale);
		//장바구니에서 판매 상품 정보
		List<ItemSet> itemList = cart.getItemSetList(); //Cart 상품 정보
		int i = 0;
		for(ItemSet is : itemList) {
			int seq = ++i;
			SaleItem saleItem = new SaleItem(sale.getSaleid(),seq,is);
			sale.getItemList().add(saleItem); //Sale 객체의 SaleItem 추가
			saleItemDao.insert(saleItem);
		}
		return sale;
	}

	public List<Sale> salelist(String id) {
		return saleDao.list(id); //사용자 id
	}

	public List<SaleItem> saleItemList(int saleid) {
		
		return saleItemDao.list(saleid); //saleid 주문번호
	}

	public void userUpdate(User user) {
		userDao.update(user);
		
	}

	public void userDelete(String userid) {
		userDao.delete(userid);
		
	}

	public List<User> userList(String[] idchks) {
		return userDao.userList(idchks);
	}

	public List<User> userList() {
		return userDao.userList();
	}

	public void boardWrite(Board board, HttpServletRequest request) {
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			uploadFileCreate(board.getFile1(), request, "board/file/");
			board.setfileurl(board.getFile1().getOriginalFilename());
		}
		int max=boardDao.maxnum();
		board.setNum(++max);
		board.setGrp(max);
		boardDao.insert(board);
		
	}

}
