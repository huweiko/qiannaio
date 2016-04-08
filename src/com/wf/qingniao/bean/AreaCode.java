/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wf.qingniao.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "areacode")  // 建议加上注解， 混淆后表名不受影响
public class AreaCode extends EntityBase {

    @Column(column = "area")
    public String area;

    @Column(column = "code")
    private String code;

    public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
    public String toString() {
        return "areacode{" +
                "id=" + getId() +
                ", area='" + area + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
