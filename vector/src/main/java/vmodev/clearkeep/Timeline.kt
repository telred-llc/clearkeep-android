//package vmodev.clearkeep
//
//interface Timeline {
//
//    var listener: Listener?
//
//    /**
//     * This should be called before any other method after creating the timeline. It ensures the underlying database is open
//     */
//    fun start()
//
//    /**
//     * This should be called when you don't need the timeline. It ensures the underlying database get closed.
//     */
//    fun dispose()
//
//    /**
//     * Check if the timeline can be enriched by paginating.
//     * @param the direction to check in
//     * @return true if timeline can be enriched
//     */
//    fun hasMoreToLoad(direction: Direction): Boolean
//
//    /**
//     * This is the main method to enrich the timeline with new data.
//     * It will call the onUpdated method from [Listener] when the data will be processed.
//     * It also ensures only one pagination by direction is launched at a time, so you can safely call this multiple time in a row.
//     */
//    fun paginate(direction: Direction, count: Int)
//
//    fun pendingEventCount() : Int
//
//    fun failedToDeliverEventCount() : Int
//
//    interface Listener {
//        /**
//         * Call when the timeline has been updated through pagination or sync.
//         * @param snapshot the most uptodate snapshot
//         */
//        fun onUpdated(snapshot: List<TimelineEvent>)
//    }
//
//    /**
//     * This is used to paginate in one or another direction.
//     */
//    enum class Direction {
//        /**
//         * It represents future events.
//         */
//        FORWARDS,
//        /**
//         * It represents past events.
//         */
//        BACKWARDS
//    }
//
//}