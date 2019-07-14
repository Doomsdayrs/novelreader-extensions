package com.github.Doomsdayrs.api.novelreader_core.extensions.lang.en;

import com.github.Doomsdayrs.api.novelreader_core.extensions.lang.en.syosetu.Syosetu;
import com.github.Doomsdayrs.api.novelreader_core.services.core.dep.Formatter;

import java.io.IOException;

/**
 * com.github.Doomsdayrs.api.novelreader_core.extensions.lang.en
 * 03 / June / 2019
 *
 * @author github.com/doomsdayrs
 */
class Test {
    public static void main(String[] args) throws IOException {
        Formatter formatter = new Syosetu(1);
        System.out.println(formatter.parseNovel("https://ncode.syosetu.com/n4544fa/").description);
    }
}
