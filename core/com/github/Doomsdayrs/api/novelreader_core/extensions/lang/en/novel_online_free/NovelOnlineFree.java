package com.github.Doomsdayrs.api.novelreader_core.extensions.lang.en.novel_online_free;

import com.github.Doomsdayrs.api.novelreader_core.services.core.dep.ScrapeFormat;
import com.github.Doomsdayrs.api.novelreader_core.services.core.objects.Novel;
import com.github.Doomsdayrs.api.novelreader_core.services.core.objects.NovelPage;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.List;

//TODO New parser, Deprecated while uncompleted.
@Deprecated
public class NovelOnlineFree extends ScrapeFormat {
    private final String baseURL = "http://novelfull.com";

    public NovelOnlineFree(int id) {
        super(id);
    }

    public NovelOnlineFree(int id, Request.Builder builder) {
        super(id, builder);
    }

    public NovelOnlineFree(int id, OkHttpClient client) {
        super(id, client);
    }

    public NovelOnlineFree(int id, Request.Builder builder, OkHttpClient client) {
        super(id, builder, client);
    }


    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getImageURL() {
        return null;
    }

    @Override
    public boolean isIncrementingChapterList() {
        return false;
    }

    @Override
    public String getNovelPassage(String s) throws IOException {
        return null;
    }

    @Override
    public NovelPage parseNovel(String s) throws IOException {
        return null;
    }

    @Override
    public NovelPage parseNovel(String s, int i) throws IOException {
        return null;
    }

    @Override
    public String getLatestURL(int i) {
        return null;
    }

    @Override
    public List<Novel> parseLatest(String s) throws IOException {
        return null;
    }

    @Override
    public List<Novel> search(String s) throws IOException {
        return null;
    }
}
