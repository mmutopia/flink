/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mmutopia.flink;

import com.mmutopia.pojo.Sensor;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;

import javax.annotation.Nullable;

/**
 * Skeleton for a Flink Streaming Job.
 *
 * <p>For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the <a href="https://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class StreamingJob {

	public static void main(String[] args) throws Exception {
		// set up the streaming execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(1);
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		/*DataStream<String> initData = env.readTextFile("" +
				"/Users/utopia/IdeaProjects/flink-mmtopia-example/src/main/resources/sensor.txt");
		*/
		DataStream<String> initData = env.socketTextStream("127.0.0.1", 9999);
		DataStream<Sensor> sensorDataStream = initData.map(new RichMapFunction<String, Sensor>() {
			@Override
			public Sensor map(String s) throws Exception {
				String[] split = s.split(",");
				return new Sensor(String.valueOf(split[0]), Long.valueOf(split[1]), Double.valueOf(split[2]), 1L);
			}
		})
				.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<Sensor>() {
					Long bound = 10000L;
					Long maxTimestamp = Long.MAX_VALUE;
					@Nullable
					@Override
					public Watermark getCurrentWatermark() {
						return new Watermark(maxTimestamp - bound);
					}
					@Override
					public long extractTimestamp(Sensor sensor, long l) {
						maxTimestamp = (sensor.getTimestamp() - maxTimestamp) > 0 ? sensor.getTimestamp() : maxTimestamp;
						return sensor.getTimestamp()*1000;
					}
				});
		DataStream<Sensor> reduceData = sensorDataStream.keyBy("sendorId")
				.timeWindow(Time.seconds(10))
				.reduce((sensor1, sensor2)->{
					return new Sensor(sensor1.getSendorId(), sensor1.getTimestamp(), sensor1.getTermp() ,sensor1.getCount() + sensor2.getCount());
				});
		reduceData.print();
		env.execute("Flink Streaming Java API Skeleton");
	}
}
