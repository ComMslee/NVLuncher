package gg.op.agro.p

import androidx.recyclerview.widget.RecyclerView
import com.nv.lutil.listener.OnEmptyListener
import com.nv.lutil.listener.OnItemClickListener

abstract class PListAdapter<ViewHolder : RecyclerView.ViewHolder?, Model> :
    RecyclerView.Adapter<ViewHolder>() {
    protected var arrayList: ArrayList<Model> = ArrayList()
    var onItemClickListener: OnItemClickListener<Model>? = null
    var onEmptyListener: OnEmptyListener? = null

    fun add(t: Model) {
        arrayList.add(t)
        chkEmpty()
    }

    fun add(t: java.util.ArrayList<Model>) {
        arrayList.addAll(t)
        chkEmpty()
    }

    fun remove(position: Int) {
        arrayList.removeAt(position)
        chkEmpty()
    }

    fun remove(t: Model) {
        arrayList.remove(t)
        chkEmpty()
    }


    fun isEmpty(): Boolean {
        return arrayList.isEmpty()
    }

    fun clear() {
        arrayList.clear()
        chkEmpty()
    }

    fun new() {
        arrayList = ArrayList()
    }

    fun get(position: Int): Model {
        return arrayList.get(position)
    }

    fun getAll(): ArrayList<Model> {
        return arrayList
    }

    private fun chkEmpty() {
        onEmptyListener?.onItemEmpty(arrayList.isEmpty())
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mapping(holder, arrayList.get(position), position)
    }

    protected abstract fun mapping(holder: ViewHolder, model: Model, position: Int)
}