package com.github.Doomsdayrs.api.novelreader_core.extensions.lang.en.box_novel;

import com.github.Doomsdayrs.api.novelreader_core.services.core.dep.ScrapeFormat;
import com.github.Doomsdayrs.api.novelreader_core.services.core.objects.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
 * novelreader-extensions
 * 11 / June / 2019
 *
 * @author github.com/doomsdayrs
 */
//TODO
public class BoxNovel extends ScrapeFormat {
    private final String baseURL = "https://boxnovel.com";

    public BoxNovel(int id) {
        super(id);
    }

    public BoxNovel(int id, Request.Builder builder) {
        super(id, builder);
    }

    public BoxNovel(int id, OkHttpClient client) {
        super(id, client);
    }

    public BoxNovel(int id, Request.Builder builder, OkHttpClient client) {
        super(id, builder, client);
    }

    @Override
    public String getName() {
        return "BoxNovel";
    }

    @Override
    public String getImageURL() {
        return "https://boxnovel.com/wp-content/uploads/2018/04/BoxNovel-1.png";
    }

    @Override
    public String getNovelPassage(String s) throws IOException {
        s = verify(baseURL, s);
        Elements paragraphs = docFromURL(s).select("div.text-left").select("p");
        StringBuilder stringBuilder = new StringBuilder();

        for (Element element : paragraphs)
            stringBuilder.append(element.toString()).append("\n");

        return stringBuilder
                .toString()
                .replaceAll("<p>", "")
                .replaceAll("</p>", "");
    }

    /**
     * TITLE:YES
     * IMAGEURL: YES
     * DESCRIPTION: YES
     * GENRES: YES
     * AUTHORS: YES
     * STATUS: YES
     * TAGS: NO
     * ARTISTS: YES
     * LANGUAGE: NO
     * MAXCHAPTERPAGE: NO
     * NOVELCHAPTERS: YES
     */
    @Override
    public NovelPage parseNovel(String s) throws IOException {
        s = verify(baseURL, s);
        NovelPage novelPage = new NovelPage();
        Document document = docFromURL(s);
        novelPage.imageURL = document.selectFirst("div.summary_image").selectFirst("img.img-responsive").attr("src");
        novelPage.title = document.selectFirst("h3").text();
        novelPage.description = document.selectFirst("p").text();

        {
            Elements elements = document.selectFirst("div.post-content").select("div.post-content_item");
            for (int x = 0; x < elements.size(); x++) {
                Elements subElements;
                switch (x) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3://AUTHORS
                        subElements = elements.get(x).select("a");
                        String[] authors = new String[subElements.size()];
                        for (int y = 0; y < subElements.size(); y++) {
                            authors[y] = subElements.get(y).text();
                        }
                        novelPage.authors = authors;
                        break;
                    case 4:
                        subElements = elements.get(x).select("a");
                        String[] artists = new String[subElements.size()];
                        for (int y = 0; y < subElements.size(); y++) {
                            artists[y] = subElements.get(y).text();
                        }
                        novelPage.artists = artists;
                        break;
                    case 5:
                        subElements = elements.get(x).select("a");
                        String[] genres = new String[subElements.size()];
                        for (int y = 0; y < subElements.size(); y++) {
                            genres[y] = subElements.get(y).text();
                        }
                        novelPage.genres = genres;
                        break;
                    case 6:
                        break;
                }
            }
            elements = document.selectFirst("div.post-status").select("div.post-content_item");
            for (int x = 0; x < elements.size(); x++) {
                switch (x) {
                    case 0:
                        break;
                    case 1:
                        String stat = elements.get(x).select("div.summary-content").text();
                        switch (stat) {
                            case "OnGoing":
                                novelPage.status = Stati.PUBLISHING;
                                break;
                            case "Completed":
                                novelPage.status = Stati.COMPLETED;
                                break;
                        }
                        break;
                    case 2:
                        break;
                }
            }

        }

        // Chapters
        {
            novelPage.novelChapters = new ArrayList<>();
            Elements elements = document.select("li.wp-manga-chapter");
            for (Element element : elements) {
                NovelChapter novelChapter = new NovelChapter();
                novelChapter.link = element.selectFirst("a").attr("href");
                novelChapter.chapterNum = element.selectFirst("a").text();
                novelChapter.release = element.selectFirst("i").text();
                novelPage.novelChapters.add(novelChapter);
            }
            Collections.reverse(novelPage.novelChapters);
        }
        return novelPage;
    }

    @Override
    public NovelPage parseNovel(String s, int i) throws IOException {
        return parseNovel(s);
    }

    @Override
    public String getLatestURL(int i) {
        return baseURL + "/novel/page/" + i + "/?m_orderby=latest";
    }

    @Override
    public List<Novel> parseLatest(String s) throws IOException {
        s = verify(baseURL, s);
        List<Novel> novels = new ArrayList<>();
        Document document = docFromURL(s);
        Elements novelsHTML = document.select("div.col-xs-12.col-md-6");
        for (Element novelHTML : novelsHTML) {
            Novel novel = new Novel();
            Element data = novelHTML.selectFirst("a");
            novel.title = data.attr("title");
            novel.link = data.attr("href");
            novel.imageURL = data.selectFirst("img").attr("src");
            novels.add(novel);
        }
        return novels;
    }

    @Override
    public List<Novel> search(String s) throws IOException {
        s = s.replaceAll("\\+", "%2");
        s = s.replaceAll(" ", "\\+");
        //Turns query into a URL
        s = baseURL + "/?s=" + s + "&post_type=wp-manga";

        List<Novel> novels = new ArrayList<>();
        Document document = docFromURL(s);
        Elements novelsHTML = document.select("div.c-tabs-item__content");

        for (Element novelHTML : novelsHTML) {
            Novel novel = new Novel();
            Element data = novelHTML.selectFirst("a");
            novel.title = data.attr("title");
            novel.link = data.attr("href");
            novel.imageURL = data.selectFirst("img").attr("src");
            novels.add(novel);
        }
        return novels;
    }

    @Override
    public NovelGenre[] getGenres() {
        return new NovelGenre[0];
    }
}
