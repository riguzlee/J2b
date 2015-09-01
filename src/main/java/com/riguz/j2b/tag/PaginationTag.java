package com.riguz.j2b.tag;

import java.io.IOException;
import java.util.Map;

import org.beetl.core.Tag;

public class PaginationTag extends Tag {

    @Override
    public void render() {
        String tagName = (String) this.args[0];
        Map attrs = (Map) args[1];
        String value = (String) attrs.get("attr");
        try {
            this.ctx.byteWriter.writeString(value);
        }
        catch (IOException e) {

        }
    }
}
