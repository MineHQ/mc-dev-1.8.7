package com.google.common.eventbus;

import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventSubscriber;

interface SubscriberFindingStrategy {
   Multimap<Class<?>, EventSubscriber> findAllSubscribers(Object var1);
}
