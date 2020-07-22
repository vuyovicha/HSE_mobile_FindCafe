package com.example.new_bottom_navigation_ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalArgumentException

class RestaurantInfoFragmentAdapter(private val man : FragmentManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var resultFragmentRows: List<ResultFragmentRow> = listOf<ResultFragmentRow>()

    companion object {
        private const val IMAGE = 0
        private const val PREVIEW = 1
        private const val ICON_ROW = 2
        private const val ACTION = 3
    }

    interface ResultFragmentRow
    class Image(val url: String) : ResultFragmentRow
    class Preview(val name: String,
                  val establishmentType : String,
                  val rating : String,
                  val minutes : String
    ) : ResultFragmentRow
    class IconRow(val iconTag: Int, val rowText : String) : ResultFragmentRow
    class Actions(val phone: String, val website : String) : ResultFragmentRow


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            IMAGE -> ImageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.restaurant_info_fragment_image, parent, false))
            PREVIEW -> PreviewViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.restaurant_map_preview, parent, false))
            ICON_ROW -> IconRowViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.restaurant_info_fragment_icon_row, parent, false))
            ACTION -> ActionsViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.restaurant_info_fragment_actions, parent, false), man)
            else -> throw IllegalArgumentException("Not supported view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (resultFragmentRows[position]) {
            is Image -> IMAGE
            is Preview -> PREVIEW
            is IconRow -> ICON_ROW
            is Actions -> ACTION
            else -> throw IllegalArgumentException("Not supported view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            IMAGE -> (holder as ImageViewHolder).onBindImage(resultFragmentRows[position] as Image)
            PREVIEW -> (holder as PreviewViewHolder).onBindPreview(resultFragmentRows[position] as Preview)
            ICON_ROW -> (holder as IconRowViewHolder).onBindIconRow(resultFragmentRows[position] as IconRow)
            ACTION -> (holder as ActionsViewHolder).onBindActions(resultFragmentRows[position] as Actions)
            else -> throw IllegalArgumentException("Not supported view type")
        }
    }

    override fun getItemCount() = resultFragmentRows.count()

    fun replaceItems(items: List<ResultFragmentRow>) {
        this.resultFragmentRows = items
        notifyDataSetChanged()
    }
}