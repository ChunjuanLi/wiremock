package com.github.tomakehurst.wiremock.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tomakehurst.wiremock.admin.Paginator;
import com.github.tomakehurst.wiremock.http.Request;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public abstract class PaginatedResult<T> {

    private final List<T> selection;
    private final Meta meta;

    protected PaginatedResult(Paginator<T> paginator) {
        selection = paginator.select();
        meta = new Meta(selection.size());
    }

    protected PaginatedResult(List<T> source, Meta meta) {
        this.selection = source;
        this.meta = meta;
    }

    protected PaginatedResult(List<T> source, Request request) {
        this(Paginator.fromRequest(source, request));
    }

    public Meta getMeta() {
        return meta;
    }

    protected List<T> select() {
        return selection;
    }

    public static class Meta {

        public final int total;

        @JsonCreator
        public Meta(@JsonProperty("total") int total) {
            this.total = total;
        }
    }
}
