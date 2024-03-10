package ir.arminapp.musicplayer.recyclerView.adapter

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import ir.arminapp.musicplayer.R
import ir.arminapp.musicplayer.activity.MusicActivity
import ir.arminapp.musicplayer.databinding.RecyclerItemBinding
import ir.arminapp.musicplayer.db.DAO.LikedDAO
import ir.arminapp.musicplayer.recyclerView.model.MusicFile
import ir.arminapp.musicplayer.utils.MusicClickData

class AdapterMusic(
    val context: Activity,
    val songData: ArrayList<MusicFile>,
    private val dao : LikedDAO,
    private val listener :MusicClickData
) : RecyclerView.Adapter<AdapterMusic.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerItemBinding.inflate(context.layoutInflater, parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int = songData.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(songData[position])
    }


    inner class ViewHolder(private val binding: RecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: MusicFile) {
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
                showMenu(it, R.menu.popup_menus ,data )
            }

            binding.root.setOnClickListener {

                listener.playMusic(data)
                val intent = Intent(context, MusicActivity::class.java)
                intent.putExtra("id", data.id)
                context.startActivity(intent)


            }


        }
    }


    private fun showMenu(v: View, @MenuRes menuRes: Int, data: MusicFile) {
        val popup = PopupMenu(context, v)

        val like = MusicFile(0 ,data.title , data.artist , data.path , data.albumArt , data.fileSize )

        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.delete -> {

                        return true
                    }
                    R.id.like ->{
                        var isLiked = false

                        if (isLiked == false){
                            val result = dao.save(like)
                            if (result == true){
                                Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()
                                isLiked = true
                                item.title = "Unlike"
                            }

                        } else{
                            dao.delete(data.id)
                            item.title = "like"

                        }

                        return true
                    }
                    R.id.trackDetails->{
                        return true
                    }
                }
                return false
            }
        })
        popup.show()



    }

}