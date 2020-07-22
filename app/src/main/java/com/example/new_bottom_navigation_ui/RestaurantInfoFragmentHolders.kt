package com.example.new_bottom_navigation_ui


import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.restaurant_info_fragment_actions.view.*
import kotlinx.android.synthetic.main.restaurant_info_fragment_icon_row.view.*
import kotlinx.android.synthetic.main.restaurant_info_fragment_image.view.*
import kotlinx.android.synthetic.main.restaurant_map_preview.view.*


class ImageViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val image = root.cafeImageLarge

    fun onBindImage(row: RestaurantInfoFragmentAdapter.Image) {
        Picasso.get().load(row.url).into(image)
    }
}

class PreviewViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val name = root.r_name
    private val rating = root.r_ratingBar
    private val time = root.r_time
    private val estType = root.r_est_type

    fun onBindPreview(row: RestaurantInfoFragmentAdapter.Preview) {
        name.text = row.name
        rating.rating = row.rating.toFloat()
        time.text = "+" + row.minutes + " mins"
        estType.text = row.establishmentType
    }
}

class IconRowViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
    private val text = root.r_text
    private val icon = root.r_icon

    companion object {
        private const val DESCRIPTION_ROW = 0
        private const val ADDRESS_ROW = 1
        private const val DIETARY_RESTRICTION_ROW = 2
        private const val PRICE_ROW = 3
        private const val CUISINE_ROW = 4
        private const val OPEN_ROW = 5
    }


    fun onBindIconRow(row: RestaurantInfoFragmentAdapter.IconRow) {
        text.text = row.rowText
        when (row.iconTag) {
            DESCRIPTION_ROW ->
                icon.setImageDrawable(root.context.getDrawable(R.drawable.ic_baseline_description_24))
            ADDRESS_ROW ->
                icon.setImageDrawable(root.context.getDrawable(R.drawable.ic_baseline_location_on_24))
            DIETARY_RESTRICTION_ROW ->
                icon.setImageDrawable(root.context.getDrawable(R.drawable.ic_baseline_eco_24))
            PRICE_ROW ->
                icon.setImageDrawable(root.context.getDrawable(R.drawable.ic_baseline_monetization_on_24))
            CUISINE_ROW ->
                icon.setImageDrawable(root.context.getDrawable(R.drawable.ic_baseline_restaurant_menu_24))
            OPEN_ROW ->{
                icon.setImageDrawable(root.context.getDrawable(R.drawable.ic_baseline_access_time_24))
                if (row.rowText.toBoolean()) text.text = "Closed"
                else text.text = "Open"
            }

        }

    }
}

class ActionsViewHolder(private val root: View, private val manager : FragmentManager) : RecyclerView.ViewHolder(root) {
    private val route = root.r_route
    private val website = root.r_website
    private val call = root.r_call

    fun onBindActions(row: RestaurantInfoFragmentAdapter.Actions) { //do not need a row here
        if (row.website.isEmpty()) {
            website.setColorFilter(Color.argb(255, 170,170,170))
            website.setBackgroundResource(R.drawable.round_border_disable)
        } else {
            website.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(row.website))
                root.context.startActivity(intent)
            }
        }

        route.setOnClickListener {

        }

        if (row.website.isEmpty()) {
            call.setColorFilter(Color.argb(255, 170,170,170))
            call.setBackgroundResource(R.drawable.round_border_disable)
        } else {
            call.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", row.phone, null))
                root.context.startActivity(intent)
            }
        }


    }
}

