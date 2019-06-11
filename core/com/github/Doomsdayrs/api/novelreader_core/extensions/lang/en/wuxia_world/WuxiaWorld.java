package com.github.Doomsdayrs.api.novelreader_core.extensions.lang.en.wuxia_world;

import com.github.Doomsdayrs.api.novelreader_core.services.core.dep.ScrapeFormat;
import com.github.Doomsdayrs.api.novelreader_core.services.core.objects.Novel;
import com.github.Doomsdayrs.api.novelreader_core.services.core.objects.NovelPage;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
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
//TODO, Requires post requests
@Deprecated
public class WuxiaWorld extends ScrapeFormat {
    public WuxiaWorld(int id) {
        super(id);
    }

    public WuxiaWorld(int id, Request.Builder builder) {
        super(id, builder);
    }

    public WuxiaWorld(int id, OkHttpClient client) {
        super(id, client);
    }

    public WuxiaWorld(int id, Request.Builder builder, OkHttpClient client) {
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
