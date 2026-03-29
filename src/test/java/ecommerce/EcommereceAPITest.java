package ecommerce;
 
import io.restassured.builder.RequestSpecBuilder;

import io.restassured.http.ContentType;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;
 
import java.io.File;

import java.util.ArrayList;

import java.util.List;
 
import io.restassured.RestAssured;
 
public class EcommereceAPITest {
 
	public static void main(String[] args) {


		RestAssured.useRelaxedHTTPSValidation();

		RequestSpecification req= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")

		 .setContentType(ContentType.JSON).build();

		LoginRequest lg= new LoginRequest();

		lg.setUserEmail("Pushkar@gmail.com");

		lg.setUserPassword("Admin@123");

		RequestSpecification requestLogin= given().log().all().spec(req).body(lg);

		LoginResponse loginResponse=requestLogin.when().post("api/ecom/auth/login").then().log().all().extract().response()

				.as(LoginResponse.class);

		String Token = loginResponse.getToken();

		String UserID= loginResponse.getUserId();


	//Add Product

		RequestSpecification addProductBaseRequest= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")

				.addHeader("Authorization", Token).build();

RequestSpecification addProduct=given().spec(addProductBaseRequest)

		.param("productName","Mobile")

		.param("productAddedBy",UserID)

		.param("productCategory","Technical")

		.param("productSubCategory","Mobile")

		.param("productPrice","11500")

		.param("productDescription","It is a Smart Phone")

		.param("productFor","men")

		.multiPart("productImage",new File("C:\\Users\\Pushkar Mishra\\Pictures\\Screenshots\\Screenshot (12).png"));
 
		ProductDetails prodDetails=addProduct.when().post("api/ecom/product/add-product").then().log().all().extract().response().as(ProductDetails.class);

		String prodID=prodDetails.getProductId();

		System.out.println("your Product Id : "+prodID);


	//Place order 

		RequestSpecification placeOrderBase= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")

				 .addHeader("Authorization", Token).setContentType(ContentType.JSON).build();

		OrderDetails od= new OrderDetails();

		od.setCountry("India");  

		od.setProductOrderedId(prodID);

		List<OrderDetails> orderlist= new ArrayList<OrderDetails>();

		orderlist.add(od);

		Orders order= new Orders();

		order.setOrders(orderlist);

		RequestSpecification createOrderReq=given().log().all().spec(placeOrderBase).body(order);

		String createOrderResp=createOrderReq.when().post("api/ecom/order/create-order").then().log().all().extract().response().asString();

		System.out.println(createOrderResp);
 
	

// Delete Product After Adding Placing the Order

	RequestSpecification DeleteProdBase= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")

				 .addHeader("Authorization", Token).build();

	RequestSpecification productDel= given().spec(DeleteProdBase).pathParam("productId",prodID);

	String Message=productDel.when().delete("api/ecom/product/delete-product/{productId}").then().log().all().extract().response().asString();

	System.out.println(Message);


	}
 
}

 