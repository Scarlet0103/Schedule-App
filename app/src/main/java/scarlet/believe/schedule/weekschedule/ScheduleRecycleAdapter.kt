package scarlet.believe.schedule.weekschedule

import android.content.res.Resources
import scarlet.believe.schedule.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.schedule_card.view.*

class ScheduleRecycleAdapter(val subjects : MutableList<String>,val timelist : MutableList<String>,val colorlist : ArrayList<Int>) : RecyclerView.Adapter<ScheduleRecycleAdapter.SubjectViewHolder>(){

    val color = R.color.light_green
    var i : Int =0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.schedule_card,parent,false)
        )
    }

    override fun getItemCount(): Int = subjects.size

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
//        if(position%2==0){
//            holder.view.card_time.text = subjects[position]
//        }
//        else{
//            holder.view.card_subject.text = subjects[position]
//            if(subjects[position].contains("LAB")){
//                holder.view.card_schdeule.setCardBackgroundColor(color)
//            }else
//            {
//                holder.view.card_schdeule.setCardBackgroundColor(colorlist[(i++)%6])
//            }
//        }
        holder.view.card_time.text = timelist[position]
        holder.view.card_subject.text = subjects[position]
        if(subjects[position].contains("LAB")){
            holder.view.card_schdeule.setCardBackgroundColor(color)
        }
        else{
            holder.view.card_schdeule.setCardBackgroundColor(colorlist[(i++)%5])
        }
    }

    class SubjectViewHolder(val view : View) : RecyclerView.ViewHolder(view)
}