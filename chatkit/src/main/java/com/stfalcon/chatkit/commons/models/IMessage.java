/*******************************************************************************
 * 版权所有 2016 stfalcon.com
 * <p>
 * 根据Apache许可证2.0版（"许可证"）进行许可；除非按照许可证的规定，否则您不得使用此文件。
 * 您可以在以下位置获得许可证的副本：
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * 除非适用法律要求或以书面形式同意，否则根据许可证分发的软件将按"原样"分发，没有任何形式的明示或暗示保证或条件。
 * 请参阅许可证，以了解管理权限和限制的具体语言。
 *******************************************************************************/

package com.stfalcon.chatkit.commons.models;

import java.util.Date;

/**
 * 用于实现真实消息模型的接口
 */
public interface IMessage {

    /**
     * 返回消息的唯一标识符
     *
     * @return 消息的ID
     */
    String getId();

    /**
     * 返回消息的文本内容
     *
     * @return 消息的文本
     */
    String getText();

    /**
     * 返回消息的作者。详情请参阅{@link IUser}
     *
     * @return 消息的作者
     */
    IUser getUser();

    /**
     * 返回消息的创建日期
     *
     * @return 消息的创建日期
     */
    Date getCreatedAt();
}
