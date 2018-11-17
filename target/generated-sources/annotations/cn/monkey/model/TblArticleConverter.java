/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package cn.monkey.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link cn.monkey.model.TblArticle}.
 *
 * NOTE: This class has been automatically generated from the {@link cn.monkey.model.TblArticle} original class using Vert.x codegen.
 */
public class TblArticleConverter {

  public static void fromJson(JsonObject json, TblArticle obj) {
    if (json.getValue("fld_content") instanceof String) {
      obj.setFld_content((String)json.getValue("fld_content"));
    }
    if (json.getValue("fld_title") instanceof String) {
      obj.setFld_title((String)json.getValue("fld_title"));
    }
  }

  public static void toJson(TblArticle obj, JsonObject json) {
    if (obj.getFld_content() != null) {
      json.put("fld_content", obj.getFld_content());
    }
    if (obj.getFld_title() != null) {
      json.put("fld_title", obj.getFld_title());
    }
    json.put("id", obj.getId());
  }
}