package com.genye.myapplication.model;

import java.io.Serializable;

/**
 * Created by yzd on 2016/5/14.
 */
public class CodeModel implements Serializable {

    private String name;
    private boolean iscomplete;
    private String code;
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean iscomplete() {
        return iscomplete;
    }

    public void setIscomplete(boolean iscomplete) {
        this.iscomplete = iscomplete;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
