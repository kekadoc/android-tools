package com.qeapp.tools.android.view.content

interface Content {

    /**
     * Показать контент
     * Вызывается что бы представить имеющийся контент
     * @param code Особый код для обработки предоставляемого контента
     */
    fun showContent(code: Int)
    /**
     * Вывод ошибки контента
     * @param code Код ошибки
     */
    fun errorContent(code: Int)
    /**
     * Показать загрузку контента
     * @param code Код загрузки
     * @return Объект-загрузка с помощью которого можно управлять загрузкой
     */
    fun loadingContent(code: Int): Loading

    interface Loading {
        /**
         * Завершить
         */
        fun complete()
        /**
         * Обновить
         */
        fun update(fraction: Float, updateData: String? = null)
    }

}