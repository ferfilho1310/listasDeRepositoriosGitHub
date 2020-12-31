package com.example.repositoriogithub.Model

import com.google.gson.annotations.SerializedName

data class ListItens(@SerializedName("total_count")
                 val totalCount: Int = 0,
                     @SerializedName("incomplete_results")
                 val incompleteResults: Boolean = false,
                     @SerializedName("items")
                 val items: List<Item>?)