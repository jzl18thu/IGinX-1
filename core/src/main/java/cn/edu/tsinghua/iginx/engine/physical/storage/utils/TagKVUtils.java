package cn.edu.tsinghua.iginx.engine.physical.storage.utils;

import cn.edu.tsinghua.iginx.engine.shared.operator.tag.*;
import cn.edu.tsinghua.iginx.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class TagKVUtils {

    public static boolean match(Map<String, String> tags, TagFilter tagFilter) {
        if (tags == null || tags.isEmpty()) {
            return false;
        }
        switch (tagFilter.getType()) {
            case And:
                return match(tags, (AndTagFilter) tagFilter);
            case Or:
                return match(tags, (OrTagFilter) tagFilter);
            case Base:
                return match(tags, (BaseTagFilter) tagFilter);
            case Precise:
                return match(tags, (PreciseTagFilter) tagFilter);
            case BasePrecise:
                return match(tags, (BasePreciseTagFilter) tagFilter);
        }
        return false;
    }

    private static boolean match(Map<String, String> tags, AndTagFilter tagFilter) {
        List<TagFilter> children = tagFilter.getChildren();
        for (TagFilter child: children) {
            if (!match(tags, child)) {
                return false;
            }
        }
        return true;
    }

    private static boolean match(Map<String, String> tags, OrTagFilter tagFilter) {
        List<TagFilter> children = tagFilter.getChildren();
        for (TagFilter child: children) {
            if (match(tags, child)) {
                return true;
            }
        }
        return false;
    }

    private static boolean match(Map<String, String> tags, BaseTagFilter tagFilter) {
        String tagKey = tagFilter.getTagKey();
        String expectedValue = tagFilter.getTagValue();
        if (!tags.containsKey(tagKey)) {
            return false;
        }
        String actualValue = tags.get(tagKey);
        if (!StringUtils.isPattern(expectedValue)) {
            return expectedValue.equals(actualValue);
        } else {
            return Pattern.matches(StringUtils.reformatPath(expectedValue), actualValue);
        }
    }

    private static boolean match(Map<String, String> tags, PreciseTagFilter tagFilter) {
        List<BasePreciseTagFilter> children = tagFilter.getChildren();
        for (TagFilter child: children) {
            if (match(tags, child)) {
                return true;
            }
        }
        return false;
    }

    private static boolean match(Map<String, String> tags, BasePreciseTagFilter tagFilter) {
        return tags.equals(tagFilter.getTags());
    }
}
