package com.github.Doomsdayrs.api.novelreader_core.extensions.lang.en.syosetu;

import com.github.Doomsdayrs.api.novelreader_core.services.core.dep.ScrapeFormat;
import com.github.Doomsdayrs.api.novelreader_core.services.core.objects.Novel;
import com.github.Doomsdayrs.api.novelreader_core.services.core.objects.NovelChapter;
import com.github.Doomsdayrs.api.novelreader_core.services.core.objects.NovelGenre;
import com.github.Doomsdayrs.api.novelreader_core.services.core.objects.NovelPage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 */

/**
 * novelreaderextensions
 * 14 / 07 / 2019
 *
 * @author github.com/doomsdayrs
 */
public class Syosetu extends ScrapeFormat {
    private final String baseURL = "https://yomou.syosetu.com";
    private final String passageURL = "https://ncode.syosetu.com";

    public Syosetu(int id) {
        super(id);
    }

    public Syosetu(int id, Request.Builder builder) {
        super(id, builder);
    }

    public Syosetu(int id, OkHttpClient client) {
        super(id, client);
    }

    public Syosetu(int id, Request.Builder builder, OkHttpClient client) {
        super(id, builder, client);
    }


    @Override
    public String getName() {
        return "Syosetu";
    }

    @Override
    public String getImageURL() {
        return "https://static.syosetu.com/view/images/common/logo_yomou.png";
    }

    @Override
    public String getNovelPassage(String s) throws IOException {
        s = verify(passageURL, s);
        Document document = docFromURL(s);
        Elements elements = document.select("div");
        boolean found = false;
        for (int x = 0; x < elements.size() && !found; x++)
            if (elements.get(x).id().equals("novel_contents")) {
                found = true;
                elements = elements.get(x).select("p");
            }

        if (found) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Element element : elements) {
                stringBuilder.append(element.text()).append("\n");
            }
            return stringBuilder.toString().replaceAll("<br>", "\n\n");
        }
        return "INVALID PARSING, CONTACT DEVELOPERS";
    }

    /**
     * TITLE: YES
     * IMAGEURL: NO
     * DESCRIPTION: YES
     * GENRES: NO
     * AUTHORS: YES
     * STATUS: NO
     * TAGS: NO
     * ARTISTS: NO
     * LANGUAGE: NO
     * MAXCHAPTERPAGE: NO
     * NOVELCHAPTERS: YES
     */
    @Override
    public NovelPage parseNovel(String s) throws IOException {
        NovelPage novelPage = new NovelPage();
        s = verify(passageURL, s);
        Document document = docFromURL(s);
        novelPage.authors = new String[]{document.selectFirst("div.novel_writername").text().replace("作者：", "")};
        novelPage.title = document.selectFirst("p.novel_title").text();
        // Description
        {
            StringBuilder stringBuilder = new StringBuilder();
            Element element = null;
            boolean found = false;
            Elements elements = document.select("div");
            for (int x = 0; x < elements.size() && !found; x++)
                if (elements.get(x).id().equals("novel_color")) {
                    element = elements.get(x);
                    found = true;
                }
            if (found) {
                String desc = element.text();
                desc = desc.replaceAll("<br>\n<br>", "\n");
                desc = desc.replaceAll("<br>", "\n");
                novelPage.description = desc;
            }
        }


        //Chapters
        {
            List<NovelChapter> novelChapters = new ArrayList<>();
            Elements elements = document.select("dl.novel_sublist2");
            for (Element element : elements) {
                NovelChapter novelChapter = new NovelChapter();
                novelChapter.chapterNum = element.selectFirst("a").text();
                novelChapter.link = passageURL + element.selectFirst("a").attr("href");
                novelChapter.release = element.selectFirst("dt.long_update").text();
                novelChapters.add(novelChapter);
            }
            novelPage.novelChapters = novelChapters;
        }
        return novelPage;
    }

    @Deprecated
    @Override
    public NovelPage parseNovel(String s, int i) throws IOException {
        return parseNovel(s);
    }

    @Override
    public String getLatestURL(int i) {
        if (i == 0)
            i = 1;
        return baseURL + "/search.php?&search_type=novel&order_former=search&order=new&notnizi=1&p=" + i;
    }

    @Override
    public List<Novel> parseLatest(String s) throws IOException {
        s = verify(baseURL, s);
        List<Novel> novels = new ArrayList<>();
        Document document = docFromURL(s);
        Elements elements = document.select("div.searchkekka_box");
        for (Element element : elements) {
            Novel novel = new Novel();
            {
                Element e = element.selectFirst("div.novel_h").selectFirst("a.tl");
                novel.link = e.attr("href");
                novel.title = e.text();
            }
            novel.imageURL = null;
            novels.add(novel);
        }
        return novels;
    }

    @Override
    public List<Novel> search(String s) throws IOException {
        s = s.replaceAll("\\+", "%2");
        s = s.replaceAll(" ", "\\+");
        s = baseURL + "/search.php?&word=" + s;
        List<Novel> novels = new ArrayList<>();
        Document document = docFromURL(s);
        Elements elements = document.select("div.searchkekka_box");
        for (Element element : elements) {
            Novel novel = new Novel();
            {
                Element e = element.selectFirst("div.novel_h").selectFirst("a.tl");
                novel.link = e.attr("href");
                novel.title = e.text();
            }
            novel.imageURL = null;
            novels.add(novel);
        }
        return novels;
    }

    @Override
    public NovelGenre[] getGenres() {
        return new NovelGenre[0];
    }
}
