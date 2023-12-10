"use client"
import { useEffect, useState } from 'react';
import ChartComponent from './ChartComponent';
import SockJs from 'sockjs-client';
import { over } from 'stompjs';

interface SensorData {
  id: number;
  data: number;
  name: string;
  timestamp: EpochTimeStamp;
}

interface SensorGraphicData {
  timestamp: EpochTimeStamp;
  data: number;
}

interface Sensor {
  name: string;
  data: SensorGraphicData[];
}

const Graphic = () => {
  const [chartData, setChartData] = useState<any>(
    {
      labels: [], 
      values: []
    },
  );

  const [sensors, setSensors] = useState<any[]>([
    //TODO: create a option to select sensors in frontend
    {
      name: "sensor1",
      data: []
    },
    {
      name: "sensor2",
      data: []
    },
    {
      name: "sensor3",
      data: []
    },
  ]);

  useEffect(() => {
    const timestamps = sensors.flatMap((sensor) =>
      sensor.data.map((dataPoint) => dataPoint.timestamp)
    );

    const values = sensors.flatMap((sensor) =>
      sensor.data.map((dataPoint) => dataPoint.data)
    );

    
    setChartData({
      labels: timestamps,
      values: values,
    });
  }, [sensors]);

  const updateSensorData = (incomingData: SensorData, prevSensors: Sensor[]) => {
    const date = new Date(incomingData.timestamp);
    const formattedDate = date.toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
    }); // Format as "dd/MM/yyyy"
    const formattedTime = date.toLocaleTimeString(); // Format as "HH:MM:SS AM/PM"

    setSensors((prevSensors) =>
      prevSensors.map((sensor) =>
        sensor.name === incomingData.name
          ? {
              ...sensor,
              data: [...sensor.data, { timestamp: formattedDate + formattedTime, data: incomingData.data }],
            }
          : sensor
      )
    );
  };

  const [stompClient, setStompClient] = useState<any>();
  const [isConnected, setIsConnected] = useState<Boolean>(false);

  const connect = () => {
    const sock = new SockJs('http://localhost:8080/ws');
    const temp = over(sock);
    setStompClient(temp);

    temp.connect({}, onConnect, onError);
  }

  const onError = (error: any) => {
    console.log(error);
  }

  const onConnect = () => {
    setIsConnected(true);
  }

  const onMessageReceived = (msg: any) => {
    const receivedMessage = JSON.parse(msg.body);

    console.log("received message: ", receivedMessage);

    updateSensorData(receivedMessage);
  }

  useEffect(() => {
    console.log("useEffect connected: ", isConnected);
    if (isConnected && stompClient) {
      console.log("connected and subscribed to /topic/public");
      isConnected === true && stompClient.subscribe("/topic/public", onMessageReceived);

      /*
      return () => {
        stompClient.unsubscribe("/topic/public", onMessageReceived);
      }
      */
    }
  }, [isConnected]);

  useEffect(() => {
    connect();
  }, []);

  return (
    <div className="flex flex-col w-full h-full">
      <div className="flex flex-row">
        <label htmlFor="sensor1">Sensor 1</label>
        <input id='sensor1' type="checkbox"></input>
      </div>

      <div className="flex flex-row">
          <ChartComponent data={chartData} />
      </div>
    </div>
  );
};

export default Graphic;