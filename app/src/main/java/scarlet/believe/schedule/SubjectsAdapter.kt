package scarlet.believe.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.notification_card.view.*

class SubjectsAdapter(val subjects : MutableList<Sub>,val colorlist : ArrayList<Int>) : RecyclerView.Adapter<SubjectsAdapter.SubjectViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.notification_card,parent,false)
        )
    }

    override fun getItemCount(): Int = subjects.size

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.view.cardtitle.text = subjects[position].sub
        holder.view.card_view.setCardBackgroundColor(colorlist[position])
    }

    class SubjectViewHolder(val view : View) : RecyclerView.ViewHolder(view)
}