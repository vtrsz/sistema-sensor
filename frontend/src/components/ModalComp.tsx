import {
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  Button,
  FormControl,
  FormLabel,
  Input,
  Box,
  Flex,
  Alert, 
  AlertIcon, 
  AlertTitle, 
  AlertDescription,
} from "@chakra-ui/react";
import { useState } from "react";
import axios from "axios";

const ModalComp = ({ data, setData, dataEdit, isOpen, onClose }) => {
  const [sequence, setSequence] = useState(dataEdit.sequence || "");
  const [sensors, setSensors] = useState(dataEdit.sensors || []);
  const [errorAlert, setErrorAlert] = useState(false);

  const backend_host = process.env.BACKEND_HOST;
  const backend_port = process.env.BACKEND_PORT;

  const validateSensors = () => {
    return sensors.every((sensor) => sensor.name.trim() !== "");
  };

  const handleClose = () => {
    setErrorAlert(false);
    onClose();
  };

  const handleSave = async () => {
    if (!sequence || !validateSensors()) {
      setErrorAlert(true);
      //console.error("Invalid data. Please fill in all fields.");
      return;
    }

    const newDataArray = !Object.keys(dataEdit).length
      ? [...(data ? data : []), { sequence, sensors }]
      : [...(data ? data : [])];

    try {
      let req;
      if (Object.keys(dataEdit).length) {
        req = await axios.put(`http://${backend_host}:${backend_port}/api/machine/${dataEdit.id}`, { sensors, sequence });
      } else {
        req = await axios.post(`http://${backend_host}:${backend_port}/api/machine`, { sensors, sequence });
      }

      // Log the response for debugging
      console.log("Server Response:", req.data);

      // Update state with the response data if it contains an id
      if (req.data.id) {
        console.log("Updating state with response data...");

        const updatedIndex = newDataArray.findIndex((item) => item.id === dataEdit.id);

        if (updatedIndex !== -1) {
          const updatedData = [...newDataArray];
          updatedData[updatedIndex] = { ...req.data, sensors, sequence };

          setData(updatedData);
        } else {
          console.log("Item not found for update");
        }
      } else {
        setData(newDataArray);
      }

      onClose();
    } catch (error) {
      console.error("Error saving data:", error);
    }
  };

  const handleInputChange = (e) => {
    // Filter out non-integer characters
    const sanitizedValue = e.target.value.replace(/[^0-9]/g, "");
    setSequence(sanitizedValue);
  };

  const handleSensorChange = (e, index) => {
    const updatedSensors = [...sensors];
    updatedSensors[index].name = e.target.value;
    setSensors(updatedSensors);
  };

  const addSensor = () => {
    setSensors([...sensors, { id: sensors.length, name: "" }]);
  };

  const removeSensor = (index) => {
    const updatedSensors = [...sensors];
    updatedSensors.splice(index, 1);
    setSensors(updatedSensors);
  };

  return (
    <>
      <Modal isOpen={isOpen} onClose={handleClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Cadastro de Sensores</ModalHeader>
          <ModalCloseButton />
          <Alert
            status="error"
            variant="subtle"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            textAlign="center"
            mb={4}
            borderRadius="md"
            display={errorAlert ? "flex" : "none"}
            zIndex="9999"
          >
            <AlertIcon boxSize="40px" mr={0} />
            <AlertTitle mt={4} mb={1} fontSize="lg">
              Invalid data. Please fill in all fields.
            </AlertTitle>
            <AlertDescription maxWidth="sm">
              There was an error with the provided data. Please ensure all fields are filled.
            </AlertDescription>
          </Alert>
          <ModalBody>
            <FormControl display="flex" flexDir="column" gap={4}>
              <Box>
                <FormLabel>Sequence</FormLabel>
                <Input
                  type="text"
                  value={sequence}
                  onChange={handleInputChange}
                />
              </Box>
              <Box>
                <FormLabel>Sensores</FormLabel>
                {sensors.map((sensor, index) => (
                  <Flex key={index} align="center" justify="space-between">
                    <Input
                      type="text"
                      value={sensor.name}
                      placeholder={`Sensor ${index + 1}`}
                      onChange={(e) => handleSensorChange(e, index)}
                    />
                    <Button colorScheme="red" onClick={() => removeSensor(index)}>
                      Remover
                    </Button>
                  </Flex>
                ))}
                <Button mt={2} onClick={addSensor}>
                  Adicionar Sensor
                </Button>
              </Box>
            </FormControl>
          </ModalBody>

          <ModalFooter justifyContent="start">
            <Button colorScheme="green" mr={3} onClick={handleSave}>
              SALVAR
            </Button>
            <Button colorScheme="red" onClick={onClose}>
              CANCELAR
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </>
  );
};

export default ModalComp;