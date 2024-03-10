package ir.arminapp.musicplayer.recyclerView.adapter

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import ir.arminapp.musicplayer.R
import ir.arminapp.musicplayer.activity.MainActivity
import ir.arminapp.musicplayer.activity.MusicActivity
import ir.arminapp.musicplayer.databinding.RecyclerItemBinding
import ir.arminapp.musicplayer.db.DAO.LikedDAO
import ir.arminapp.musicplayer.recyclerView.model.MusicFile

class AdapterLike(
    val context: Activity,
    val likeData: ArrayList<MusicFile>,
    private val dao: LikedDAO
) : RecyclerView.Adapter<AdapterLike.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerItemBinding.inflate(context.layoutInflater, parent, false)
        return ViewHolder(binding)

    }


    override fun getItemCount(): Int = likeData.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(likeData[position], position)
    }


    inner class ViewHolder(private val binding: RecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(data: MusicFile , position: Int) {
            binding.nameText.text = data.title
            binding.textArtist.text = data.artist

            val imageView: ImageView = binding.songImage
            val imagePath = data.albumArt

            // استفاده از Coil برای نمایش تصویر

            val request = ImageRequest.Builder(context)
                .data(imagePath)
                .target(
                    onSuccess = { result ->
                        // تصویر با موفقیت دریافت شد، نمایش در ImageView
                        imageView.setImageDrawable(result)
                    },
                    onError = { error ->
                        // خطا در دریافت تصویر، می‌توانید اقدامات مناسب را انجام دهید
                        imageView.setImageResource(R.drawable.placeholder)
                    }
                )
                .build()

            // اجرای ImageRequest با استفاده از ImageLoader
            val imageLoader = imageView.context.imageLoader
            imageLoader.enqueue(request)


            binding.moreImage.setOnClickListener {
                showMenu(it, R.menu.poupup_like, data , position)
            }
            binding.root.setOnClickListener {




                val intent = Intent(context, MusicActivity::class.java)
                intent.putExtra("id", data.id)
                context.startActivity(intent)
            }


        }
    }


    private fun showMenu(v: View, @MenuRes menuRes: Int, data: MusicFile, position: Int) {
        val popup = PopupMenu(context, v)

        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnDismissListener {
            // Respond to popup being dismissed.

        }
        // Show the popup menu.
        // Show the popup menu.
        popup.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.delete -> {
                    dao.delete(data.id)
                    likeData.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position , likeData.size)
                }
            }
            false
        }
        popup.show()

    }
}