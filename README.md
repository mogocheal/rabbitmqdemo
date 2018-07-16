# rabbitmqdemo
demo for  springboot rabbitmq  
 生产者手动确认（act模式）  
 publisher-confirms: true   仅仅标示消息已被Broker接收到，并不表示已成功投放至消息队列中  
 publisher-returns: true  当消息发送出去找不到对应路由队列时，将会把消息退回  
 延迟队列，死信队列，重试队列（增大channelsize）  
 消息的TTL（Time To Live）和队列的TTL  
 死信队列：
1. 一个消息被Consumer拒收了，并且reject方法的参数里requeue是false。也就是说不会被再次放在队列里，被其他消费者使用。
2. 上面的消息的TTL到了，消息过期了。
3. 队列的长度限制满了。排在前面的消息会被丢弃或者扔到死信路由上。
缓存 ConcurrentHashMap+AtomicInteger 或者redis
