package com.example.administrator.live.utils.fixtures

import com.example.administrator.live.bean.Commodity
import com.example.administrator.live.bean.Music
import com.example.administrator.live.bean.NewFans
import com.example.administrator.live.bean.Notice
import com.example.administrator.live.bean.Video
import com.example.administrator.live.utils.OkHttpUtils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.security.SecureRandom
import java.util.Date
import kotlin.math.max

/**
 * Created by Anton Bevza on 1/13/17.
 */
abstract class FixturesData {

    /**
     * 随机数生成器，用于后续生成随机数据
     */
    companion object {
        val rnd = SecureRandom()

        /**
         * 用户头像URL列表
         */
        val avatars = listOf(
            "https://c-ssl.dtstatic.com/uploads/blog/202304/15/20230415081411_f2e46.thumb.400_0.jpg",
            "https://img.keaitupian.cn/uploads/2021/03/02/4842f65de0df4644842b0dda16858210.jpg",
            "https://img.keaitupian.cn/newupload/03/1680166213150590.jpg",
            "https://ss2.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1676065055,2828606542&fm=253&gp=0.jpg"
        )

        /**
         * 群聊图片URL列表
         */
        val GROUP_CHAT_IMAGES = listOf(
            "https://i.imgur.com/hRShCT3.png",
            "https://i.imgur.com/zgTUcL3.png",
            "https://i.imgur.com/mRqh5w1.png"
        )

        /**
         * 群聊标题列表
         */
        val GROUP_CHAT_TITLES = listOf(
            "相亲相爱一家人",
            "幸福美满",
            "我的小家"
        )

        /**
         * 消息内容列表
         */
        private val MESSAGES = listOf(
            "你好!",
            "这是我的电话号码16602759414",
            "这是我的邮箱zhonglimo11@gmail.com",
            "www.baidu.com",
            "晚上好啊",
            "1、消费者与商家沟通以会话的形式、且包含语音、图片、拍小视频等",
            "亲您好，有什么可以帮到您的吗? 小客服在线为您处理解答哦!",
            "我在三里屯MAX等你",
            "亲，请您核对订单信息。",
            "[emoji_00]",
            "啊这[emoji_06]"
        )

        /**
         * 图片URL列表
         */
        val IMAGES = listOf(
            "https://c-ssl.dtstatic.com/uploads/blog/202304/15/20230415081411_f2e46.thumb.400_0.jpg",
            "https://img.keaitupian.cn/uploads/2021/03/02/4842f65de0df4644842b0dda16858210.jpg",
            "https://img.keaitupian.cn/newupload/03/1680166213150590.jpg",
            "https://ss2.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1676065055,2828606542&fm=253&gp=0.jpg",
            "https://pic.wenwen.soso.com/p/20090816/20090816152521-1568446759.jpg",
            "https://moegirl.uk/images/a/a9/Kumoito.jpg",
            "https://img.moegirl.org.cn/common/f/f0/8pGRdRhjX3o.jpg",
            "https://img.moegirl.org.cn/common/4/46/898570.jpg",
            "https://img.moegirl.org.cn/common/e/e1/%E5%BE%8C%E3%82%8D%E5%A7%BF_rose2008_201603292249.jpg"
        )

        private val VIDEOS = listOf(
            "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4",
            "https://www.w3schools.com/html/mov_bbb.mp4",
            "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        )

        private val MUSIC = listOf(
            Music(
                "1",
                "散花",
                "https://music.163.com/song/media/outer/url?id=26131697.mp3",
                "https://pic.wenwen.soso.com/p/20090816/20090816152521-1568446759.jpg",
                "水月陵",
                getRandomInt(999999999),
                getRandomBoolean(),
                216

            ),
            Music(
                "2",
                "蜘蛛糸モノポリー",
                "https://music.163.com/song/media/outer/url?id=26440351.mp3",
                "https://moegirl.uk/images/a/a9/Kumoito.jpg",
                "sasakure.UK",
                getRandomInt(999999999),
                getRandomBoolean(),
                275
            ),
            Music(
                "3",
                "妄想感傷代償連盟",
                "https://music.163.com/song/media/outer/url?id=432486474.mp3",
                "https://img.moegirl.org.cn/common/f/f0/8pGRdRhjX3o.jpg",
                "DECO*27",
                getRandomInt(999999999),
                getRandomBoolean(),
                270
            ),
            Music(
                "4",
                "ワールドイズマイン",
                "https://music.163.com/song/media/outer/url?id=22677570.mp3",
                "https://img.moegirl.org.cn/common/4/46/898570.jpg",
                "ryo(supercell)",
                getRandomInt(999999999),
                getRandomBoolean(),
                255
            ),
            Music(
                "5",
                "ツギハギスタッカート",
                "https://music.163.com/song/media/outer/url?id=30148963.mp3",
                "https://img.moegirl.org.cn/common/e/e1/%E5%BE%8C%E3%82%8D%E5%A7%BF_rose2008_201603292249.jpg",
                "とあ",
                getRandomInt(999999999),
                getRandomBoolean(),
                250
            ),
        )

        /**
         * 图片URL列表
         */
        private val E_TER_COMMENTS = listOf(
            "@JINO @方可 这个好好笑",
            "@JINO @张三 wc",
            "@JINO @方可 逆天",
            "@JINO @方可 牛逼",

            )

        data class ApiResponse(val code: Int, val msg: String)

        /**
         * 获取随机昵称
         */
        suspend fun getNicknameFromApi(): String {
            return withContext(Dispatchers.IO) {
                try {
                    val url = "https://cn.apihz.cn/api/zici/sjwm.php?id=88888888&key=88888888"
                    // 使用 OkHttpUtils 进行网络请求
                    val responseBody = OkHttpUtils.get(url)
                    val apiResponse = Gson().fromJson(responseBody, ApiResponse::class.java)
                    apiResponse.msg
                } catch (e: IOException) {
                    e.printStackTrace()
                    "Error fetching Chinese characters"
                }
            }
        }

        /**
         * 随机一言
         * 从 API 接口获取随机一句话
         */
        suspend fun getStringFromApi(): String {
            return withContext(Dispatchers.IO) {
                try {
                    val url = "https://uapis.cn/api/say"
                    // 使用 OkHttpUtils 进行网络请求
                    OkHttpUtils.get(url)
                } catch (e: IOException) {
                    e.printStackTrace()
                    "Error fetching random string"
                }
            }
        }

        /**
         * 生成随机ID
         *
         * @return 随机生成的字符串ID
         */
        fun getRandomId(): String {
            return rnd.nextInt(20).toString()
        }

        fun getRandomGroupId(): String {
            return rnd.nextInt(20).toString()
        }

        /**
         * 获取随机用户头像URL
         *
         * @return 随机选择的用户头像URL
         */
        fun getRandomAvatar(): String {
            return avatars[rnd.nextInt(avatars.size)]
        }

        /**
         * 随机生成一组图片
         */
        fun getRandomImages(number: Int = getRandomInt(10)): List<String> {
            val imagesList = ArrayList<String>()
            repeat(number) {
                val image = getRandomImage()
                imagesList.add(image)
            }
            return imagesList
        }

        /**
         * 获取随机1-7字中文昵称
         *
         * @return 随机生成的随机1-7字中文昵称
         */
        fun generateRandomChineseName(): String {
            val sb = StringBuilder()
            repeat(max(1, getRandomInt(7))) {
                val randomCodePoint =
                    (0x4E00 + (Math.random() * (0x9FA5 - 0x4E00)).toInt()).toChar()
                sb.append(randomCodePoint)
            }
            return sb.toString()
//            return getNicknameFromApi()
        }


        /**
         * 获取随机群聊图片URL
         *
         * @return 随机选择的群聊图片URL
         */
        fun getRandomGroupChatImage(): String {
            return GROUP_CHAT_IMAGES[rnd.nextInt(GROUP_CHAT_IMAGES.size)]
        }

        /**
         * 获取随机群聊标题
         *
         * @return 随机选择的群聊标题
         */
        fun getRandomGroupChatTitle(): String {
            return GROUP_CHAT_TITLES[rnd.nextInt(GROUP_CHAT_TITLES.size)]
        }

        /**
         * 获取随机消息内容
         *
         * @return 随机选择的消息内容
         */
        fun getRandomMessage(): String {
            return MESSAGES[rnd.nextInt(MESSAGES.size)]
        }

        /**
         * 获取随机图片URL
         *
         * @return 随机选择的图片URL
         */
        fun getRandomImage(): String {
            return IMAGES[rnd.nextInt(IMAGES.size)]
        }

        /**
         * 生成随机布尔值
         *
         * @return 随机的布尔值
         */
        fun getRandomBoolean(): Boolean {
            return rnd.nextBoolean()
        }

        fun getNewFansList(): ArrayList<NewFans> {
            val newFansList = ArrayList<NewFans>()
            repeat(10) {
                val newFans = NewFans(getRandomId(), DialogsFixtures.getUser())
                newFansList.add(newFans)
            }
            return newFansList
        }

        fun getNoticeList(): ArrayList<Notice> {
            val noticeList = ArrayList<Notice>()
            repeat(10) {
                val notice = Notice(getRandomId(), DialogsFixtures.getUser(), "newSubordinate")
                noticeList.add(notice)
            }
            return noticeList
        }

        fun getETerMessage(): String {
            return E_TER_COMMENTS[rnd.nextInt(E_TER_COMMENTS.size)]
        }

        fun getRandomInt(int: Int): Int {
            return rnd.nextInt(int)
        }

        fun getRandomFloat(): Float {
            val randomFloat = rnd.nextFloat() * 1000 // 生成0到1000之间的随机浮点数
            return String.format("%.1f", randomFloat).toFloat()
        }

        /**
         * 获取从当前时间减少随机时间
         */
        fun getRandomDate(): Date {
            return Date(System.currentTimeMillis() - rnd.nextInt(100000))
        }

        /**
         * 获取从当前时间减少随机毫秒数
         */
        fun getRandomMillis(): Long {
            return System.currentTimeMillis() - rnd.nextInt(100000)
        }
    }

    private fun getRandomVideoUrl(): String {
        return VIDEOS[rnd.nextInt(VIDEOS.size)]
    }

    /**
     * 随机生成一个视频
     */
    fun getOneVideo(): Video {
        val id = getRandomId()
        val video = Video(id.toInt(), title = "测试")
        return video
    }

    /**
     * 随机生成视频列表
     */
    fun getVideos(number: Int = 10): ArrayList<Video> {
        val videoList = ArrayList<Video>()
        repeat(rnd.nextInt(number)) {
            videoList.add(getOneVideo())
        }
        return videoList
    }

    /**
     * 随机生成一个音乐
     */
    fun getMusic(): Music {
        return MUSIC[rnd.nextInt(MUSIC.size)]
    }

    fun getMusics(): ArrayList<Music>{
        val list = ArrayList<Music>()
        list.addAll(MUSIC)
        return list
    }

    /**
     * 随机生成一个商品
     */
    fun getCommodity(isLike: Boolean = getRandomBoolean()): Commodity {
        return Commodity(
            id = getRandomId(),
            name = "商品${getRandomInt(100)}",
            price = getRandomFloat(),
            cover = getRandomImage(),
            isLike = isLike,
            likeCount = getRandomInt(999999),
            isFailure = getRandomBoolean()
        )
    }

    fun getCommodities(number: Int = 10): ArrayList<Commodity> {
        val commodityList = ArrayList<Commodity>()
        repeat(rnd.nextInt(number)) {
            commodityList.add(getCommodity())
        }
        return commodityList
    }

    fun getFavoriteCommodities(number: Int = 10): ArrayList<Commodity> {
        val commodityList = ArrayList<Commodity>()
        repeat(rnd.nextInt(number)) {
            commodityList.add(getCommodity(true))
        }
        return commodityList
    }
}