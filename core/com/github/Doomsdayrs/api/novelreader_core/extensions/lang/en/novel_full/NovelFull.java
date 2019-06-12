package com.github.Doomsdayrs.api.novelreader_core.extensions.lang.en.novel_full;

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

/**
 * This file is part of novelreader-extensions.
 * novelreader-extensions is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with novelreader-extensions.  If not, see <https://www.gnu.org/licenses/>.
 * ====================================================================
 * novelreader-extensions
 * 30 / May / 2019
 *
 * @author github.com/doomsdayrs
 */
public class NovelFull extends ScrapeFormat {
    private final String baseURL = "http://novelfull.com";

    public NovelFull(int id) {
        super(id);
    }

    public NovelFull(int id, Request.Builder builder) {
        super(id, builder);
    }

    public NovelFull(int id, OkHttpClient client) {
        super(id, client);
    }

    public NovelFull(int id, Request.Builder builder, OkHttpClient client) {
        super(id, builder, client);
    }

    @Override
    public String getName() {
        return "NovelFull";
    }


    @Override
    public String getImageURL() {
        return null;
    }

    private void stripListing(Elements data, Novel novel) {
        for (int y = 0; y < data.size(); y++) {
            Element coloum = data.get(y);
            switch (y) {
                case 0: {
                    Element image = coloum.selectFirst("img");
                    if (image != null)
                        novel.imageURL = baseURL + image.attr("src");
                }
                case 1: {
                    Element header = coloum.selectFirst("h3");
                    if (header != null) {
                        Element titleLink = header.selectFirst("a");
                        novel.title = titleLink.attr("title");
                        novel.link = baseURL + titleLink.attr("href");
                    }
                }
                default:
                    break;
            }
        }
    }

    // Overriding methods

    public boolean isIncrementingChapterList() {
        return true;
    }

    public String getNovelPassage(String URL) throws IOException {
        URL = verify(baseURL, URL);
        Document document = docFromURL(URL);
        Elements paragraphs = document.select("div.chapter-c").select("p");
        StringBuilder stringBuilder = new StringBuilder();
        for (Element element : paragraphs)
            stringBuilder.append(element.text()).append("\n");

        return stringBuilder.toString();
    }

    public NovelPage parseNovel(String URL) throws IOException {
        URL = verify(baseURL, URL);
        return this.parseNovel(URL, 1);
    }

    public NovelPage parseNovel(String URL, int increment) throws IOException {
        URL = verify(baseURL, URL);
        if (increment > 1)
            URL = URL + "?page=" + increment;
        Document document = docFromURL(URL);
        NovelPage novelPage = new NovelPage();

        //Sets image
        novelPage.imageURL = baseURL + document.selectFirst("div.book").selectFirst("img").attr("src");


        //Sets description
        {
            Element titleDescription = document.selectFirst("div.col-xs-12.col-sm-8.col-md-8.desc");
            novelPage.title = titleDescription.selectFirst("h3").text();

            Element description = titleDescription.selectFirst("div.desc-text");
            Elements text = description.select("p");
            StringBuilder stringBuilder = new StringBuilder();
            for (Element paragraph : text) {
                stringBuilder.append(paragraph.text()).append("\n");
            }
            novelPage.description = stringBuilder.toString();
        }

        //Formats the chapters
        {
            List<NovelChapter> novelChapters = new ArrayList<>();
            Elements lists = document.select("ul.list-chapter");
            for (Element list : lists) {
                Elements chapters = list.select("li");
                int a = 0;
                for (Element chapter : chapters) {
                    NovelChapter novelChapter = new NovelChapter();
                    Element chapterData = chapter.selectFirst("a");
                    String link = chapterData.attr("href");
                    if (link != null)
                        novelChapter.link = baseURL + link;

                    novelChapter.chapterNum = chapterData.attr("title");
                    if (!novelChapter.chapterNum.isEmpty() && !novelChapter.link.contains("null")) {
                        novelChapters.add(novelChapter);
                    }
                }

            }
            novelPage.novelChapters = novelChapters;
        }


        return novelPage;
    }

    public String getLatestURL(int page) {
        return baseURL + "/latest-release-novel?page=" + page;
    }

    public List<Novel> parseLatest(String URL) throws IOException {
        URL = verify(baseURL, URL);
        List<Novel> novels = new ArrayList<>();
        Document document = docFromURL(URL);
        Elements divMAIN = document.select("div.container");
        for (Element element : divMAIN) {
            if (element.id().equals("list-page")) {
                Element list = element.selectFirst("div");
                Elements releases = list.select("div.row");
                //For each novel
                for (Element release : releases) {
                    Novel novel = new Novel();
                    Elements data = release.select("div");
                    //For each coloum
                    this.stripListing(data, novel);
                    if (novel.link != null && novel.title != null)
                        novels.add(novel);
                }
            }
        }
        return novels;
    }

    @Override
    public List<Novel> search(String query) throws IOException {
        List<Novel> novels = new ArrayList<>();
        Document document = docFromURL(baseURL + "/search?keyword=" + query.replaceAll(" ", "%20"));
        Elements listP = document.select("div.container");
        for (Element list : listP)
            if (list.id().equals("list-page")) {
                Elements queries = list.select("div.row");
                for (Element q : queries) {
                    Novel novel = new Novel();
                    this.stripListing(q.select("div"), novel);
                    if (novel.link != null && novel.title != null)
                        novels.add(novel);
                }
            }
        return novels;
    }


    @Override
    public NovelGenre[] getGenres() {
        String url = baseURL + "/genre/";
        return new NovelGenre[]{
                new NovelGenre("Shounen", true, url + "Shounen"),
                new NovelGenre("Harem", true, url + "Harem"),
                new NovelGenre("Comedy", true, url + "Comedy"),
                new NovelGenre("Martial Arts", true, url + "Martial Arts"),
                new NovelGenre("School Life", true, url + "School Life"),
                new NovelGenre("Mystery", true, url + "Mystery"),
                new NovelGenre("Shoujo", true, url + "Shoujo"),
                new NovelGenre("Romance", true, url + "Romance"),
                new NovelGenre("Sci-fi", true, url + "Sci-fi"),
                new NovelGenre("Gender Bender", true, url + "Gender Bender"),
                new NovelGenre("Mature", true, url + "Mature"),
                new NovelGenre("Fantasy", true, url + "Fantasy"),
                new NovelGenre("Horror", true, url + "Horror"),
                new NovelGenre("Drama", true, url + "Drama"),
                new NovelGenre("Tragedy", true, url + "Tragedy"),
                new NovelGenre("Supernatural", true, url + "Supernatural"),
                new NovelGenre("Ecchi", true, url + "Ecchi"),
                new NovelGenre("Xuanhuan", true, url + "Xuanhuan"),
                new NovelGenre("Adventure", true, url + "Adventure"),
                new NovelGenre("Action", true, url + "Action"),
                new NovelGenre("Psychological", true, url + "Psychological"),
                new NovelGenre("Xianxia", true, url + "Xianxia"),
                new NovelGenre("Wuxia", true, url + "Wuxia"),
                new NovelGenre("Historical", true, url + "Historical"),
                new NovelGenre("Slice of Life", true, url + "Slice of Life"),
                new NovelGenre("Seinen", true, url + "Seinen"),
                new NovelGenre("Lolicon", true, url + "Lolicon"),
                new NovelGenre("Adult", true, url + "Adult"),
                new NovelGenre("Josei", true, url + "Josei"),
                new NovelGenre("Sports", true, url + "Sports"),
                new NovelGenre("Smut", true, url + "Smut"),
                new NovelGenre("Mecha", true, url + "Mecha"),
        };
    }


}
