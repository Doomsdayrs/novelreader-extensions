package com.github.Doomsdayrs.api.novelreader_core.extensions.lang.en;

import com.github.Doomsdayrs.api.novelreader_core.extensions.lang.en.novel_full.NovelFull;
import com.github.Doomsdayrs.api.novelreader_core.services.core.dep.Formatter;
import com.github.Doomsdayrs.api.novelreader_core.services.core.objects.Novel;
import com.github.Doomsdayrs.api.novelreader_core.services.core.objects.NovelPage;

import java.io.IOException;
import java.util.List;

/**
 * com.github.Doomsdayrs.api.novelreader_core.extensions.lang.en
 * 03 / June / 2019
 *
 * @author github.com/doomsdayrs
 */
class Test {
    public static void main(String[] args) throws IOException {
        Formatter formatter = new NovelFull(1);
        List<Novel> novels = formatter.search("overlord");
        NovelPage novelPage = formatter.parseNovel(novels.get(0).link, 1);
        System.out.println(novelPage);
        novelPage = formatter.parseNovel(novels.get(0).link, 2);
        System.out.println(novelPage);

    }
}