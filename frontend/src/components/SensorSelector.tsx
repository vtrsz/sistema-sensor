"use client"
import { 
  Select,
  Button,
  FormControl, 
  FormLabel, 
  Box, 
  Modal, 
  ModalOverlay, 
  ModalContent, 
  ModalHeader, 
  ModalBody, 
  ModalFooter, 
  ModalCloseButton,
  useDisclosure,
  Flex,
} from "@chakra-ui/react";
import { useEffect, useState } from 'react';
import axios from "axios";
import GraphicsModal from "./GraphicsModal";

const SensorSelector = () => {
    const [sensors, setSensors] = useState<any[]>([]);
    const { isOpen, onOpen, onClose } = useDisclosure();
    const [selectedSensor, setSelectedSensor] = useState("");
    const [dataEdit, setDataEdit] = useState({});

    useEffect(() => {
      const fetchData = async () => {
        try {
          const response = await axios.get("http://localhost:8080/api/sensor");
          setSensors(response.data);
        } catch (error) {
          console.error("Error fetching data:", error);
        }
      };

      fetchData();
    }, [setSensors]);
    

    const handleViewGraphics = () => {
      if (selectedSensor === "") {
        alert("Selecione um sensor");
        return;
      }

      const selectedSensorId = parseInt(selectedSensor);

      if (isNaN(selectedSensorId)) {
        console.error("Invalid selected sensor ID:", selectedSensor);
        return;
      }

      const selectedSensorData = sensors.find((sensor) => sensor.id === selectedSensorId);

      if (selectedSensorData) {
        // Access other properties
        const { id, name, machineId } = selectedSensorData;
    
        setDataEdit({
          id,
          name,
          machineId,
        })
    
        // Open the modal
        onOpen();
      } else {
        console.error("Selected sensor not found in the sensors array");
      }      

    };

    return (<>
      <Flex justifyContent="center" alignItems="center">
        <Box>
          <FormControl display="flex" justifyContent="center" textAlign="center">
            <Box>
              <FormLabel textAlign="center">Sensores</FormLabel>
              <Select
                placeholder="Select a sensor"
                value={selectedSensor}
                onChange={(e) => setSelectedSensor(e.target.value)}
              >
                {sensors.map((sensor) => (
                  <option key={sensor.id} value={sensor.id}>
                    {sensor.id + " - " + sensor.name + " - " + sensor.machineId}
                  </option>
                ))}
              </Select>
            </Box>
          </FormControl>

          <Button justifyContent="center" alignItems="center" colorScheme="blue"  onClick={handleViewGraphics} mt={4}>
            Ver Gráficos
          </Button>
        </Box>
      </Flex>
      {isOpen && (
        <GraphicsModal
          isOpen={isOpen}
          onClose={onClose}
          selectedSensor={selectedSensor}
          dataEdit={dataEdit}
          setDataEdit={setDataEdit}
        />
      )}
    </>)
}

export default SensorSelector;