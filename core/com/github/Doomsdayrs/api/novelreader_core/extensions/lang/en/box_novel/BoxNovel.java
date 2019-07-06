package com.github.Doomsdayrs.api.novelreader_core.extensions.lang.en.box_novel;

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
import java.util.Collections;
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
        Elements paragraphs = docFromURL(s).select("div.text-left");
        StringBuilder stringBuilder = new StringBuilder();
        for (Element element : paragraphs) {
            stringBuilder.append(element.toString());
        }

        return stringBuilder.toString().replaceAll("<p>", "").replaceAll("</p>", "").replaceAll("<br>", "\n");
    }

    @Override
    public NovelPage parseNovel(String s) throws IOException {
        s = verify(baseURL, s);
        NovelPage novelPage = new NovelPage();
        Document document = docFromURL(s);
        novelPage.imageURL = document.selectFirst("div.summary_image").selectFirst("img.img-responsive").attr("src");
        novelPage.title = document.selectFirst("h3").text();
        novelPage.description = document.selectFirst("p").text();
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
