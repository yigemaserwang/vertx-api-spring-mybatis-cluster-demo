package cn.monkey.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class TblArticle {

	private final int id;

	private String fld_title;

	public String getFld_title() {
		return fld_title;
	}

	public void setFld_title(String fld_title) {
		this.fld_title = fld_title;
	}

	public String getFld_content() {
		return fld_content;
	}

	public void setFld_content(String fld_content) {
		this.fld_content = fld_content;
	}

	public int getId() {
		return id;
	}

	private String fld_content;

	public TblArticle(String title, String content) {
		this.fld_title = title;
		this.fld_content = content;
		this.id = -1;
	}

	public TblArticle(JsonObject json) {
		this.fld_title = json.getString("FLD_TITLE");
		this.fld_content = json.getString("FLD_CONTENT");
		this.id = json.getInteger("ID");
	}

	public TblArticle() {
		this.id = -1;
	}

	public TblArticle(int id, String title, String content) {
		this.id = id;
		this.fld_title = title;
		this.fld_content = content;
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		TblArticleConverter.toJson(this, json);
		return json;
	}

}