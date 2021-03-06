/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.zhulinping.emojidemo.utils;

import android.text.TextUtils;

/**
 * The string parser of codesArray specification for <GridRows />. The attribute codesArray is an
 * array of string.
 * Each element of the array defines a key label by specifying a code point as a hexadecimal string.
 * A key label may consist of multiple code points separated by comma.
 * Each element of the array optionally can have an output text definition after vertical bar
 * marker. An output text may consist of multiple code points separated by comma.
 * The format of the codesArray element should be:
 * <pre>
 *   codePointInHex[,codePoint2InHex]*(|outputTextCodePointInHex[,outputTextCodePoint2InHex]*)?
 * </pre>
 */
// TODO: Write unit tests for this class.
public final class CodesArrayParser {
    // Constants for parsing.
    private static final char COMMA = ',';
    private static final String VERTICAL_BAR_STRING = "\\|";
    public static final String COMMA_STRING = ",";
    private static final int BASE_HEX = 16;

    private CodesArrayParser() {
        // This utility class is not publicly instantiable.
    }

    private static String getLabelSpec(final String codesArraySpec) {
        final String[] strs = codesArraySpec.split(COMMA_STRING, -1);
        if (strs.length <= 1) {
            return codesArraySpec;
        }
        return strs[0];
    }

    /**
     * 获取多肤色分割字符
     *
     * @param codesArraySpec
     * @return
     */
    public static String[] getSkinSlit(final String codesArraySpec) {
        if (TextUtils.isEmpty(codesArraySpec)) {
            return null;
        }
        final String[] strs = codesArraySpec.split(COMMA_STRING, -1);
        final String[] dest = new String[strs.length - 1];
        System.arraycopy(strs, 1, dest, 0, dest.length);
        return dest;
    }

    //Key:表示发送的Code
    public static String parseLabel(final String codesArraySpec) {
        final String labelSpec = getLabelSpec(codesArraySpec);
        final StringBuilder sb = new StringBuilder();
        try {
            for (final String codeInHex : labelSpec.split(VERTICAL_BAR_STRING)) {
                final int codePoint = Integer.parseInt(codeInHex, BASE_HEX);
                sb.appendCodePoint(codePoint);
            }
            return sb.toString();
        }catch (Exception e){
            //catch到不能转换为16进制的异常（即不是表情），返回null
            return null;
        }
    }
    //关联图片资源名字
    public static String getLabelRes(final String codesArraySpec) {
        final String labelSpec = getLabelSpec(codesArraySpec);
        final StringBuilder sb = new StringBuilder();
        for (final String str : labelSpec.split(VERTICAL_BAR_STRING)) {
            sb.append("_").append(str);
        }
        return sb.toString();
    }

    private static String getCodeSpec(final String codesArraySpec) {
        final String[] strs = codesArraySpec.split(VERTICAL_BAR_STRING, -1);
        if (strs.length <= 1) {
            return codesArraySpec;
        }
        return TextUtils.isEmpty(strs[1]) ? strs[0] : strs[1];
    }

    // codesArraySpec consists of:
    // <label>|<code0>,<code1>,...|<minSupportSdkVersion>
    public static int getMinSupportSdkVersion(final String codesArraySpec) {
        final String[] strs = codesArraySpec.split(VERTICAL_BAR_STRING, -1);
        if (strs.length <= 2) {
            return 0;
        }
        try {
            return Integer.parseInt(strs[2]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /*public static int parseCode(final String codesArraySpec) {
        final String codeSpec = getCodeSpec(codesArraySpec);
        if (codeSpec.indexOf(COMMA) < 0) {
            return Integer.parseInt(codeSpec, BASE_HEX);
        }
        return Constants.CODE_OUTPUT_TEXT;
    }*/

    public static String parseOutputText(final String codesArraySpec) {
        final String codeSpec = getCodeSpec(codesArraySpec);
        if (codeSpec.indexOf(COMMA) < 0) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        for (final String codeInHex : codeSpec.split(COMMA_STRING)) {
            final int codePoint = Integer.parseInt(codeInHex, BASE_HEX);
            sb.appendCodePoint(codePoint);
        }
        return sb.toString();
    }
}
