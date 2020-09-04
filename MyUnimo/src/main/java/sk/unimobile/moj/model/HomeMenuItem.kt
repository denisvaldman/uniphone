package sk.unimobile.moj.model

class HomeMenuItem (title:String, action:Int){

    companion object {
        public const val HOME_MENU_PAYMENTS = 0
        public const val HOME_MENU_USAGE = 1
        public const val HOME_MENU_SETTINGS = 2
    }

    private var title: String = title
    private var action: Int = -1

    init {
        this.action = action
    }

    fun getTitle(): String{
        return title
    }

    fun action() {
    }




}