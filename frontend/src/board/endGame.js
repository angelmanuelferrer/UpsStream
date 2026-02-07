
import BackButton from "./BackButton";

export default function EndGame({ winner }) {
  
  if (!winner || !winner.username) {
    return (
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          alignItems: 'center',
          height: '100vh',
          backgroundColor: '#f0f0f0', // Fondo de pantalla gris claro
        }}
      >
        <h1
          style={{
            fontSize: '2rem',
            fontWeight: 'bold',
            marginBottom: '1rem',
            color: '#e74c3c', // Rojo para destacar el error
            textTransform: 'uppercase',
          }}
        >
          No winner data available!
        </h1>
      </div>
    );
  }

  
  return (
    <div
      style={{
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        backgroundColor: '#f0f0f0', 
      }}
    >
      <h1
        style={{
          fontSize: '2rem',
          fontWeight: 'bold',
          marginBottom: '1rem',
          color: '#2c3e50',
          textTransform: 'uppercase',
        }}
      >
        The winner is!!!!!
      </h1>
      <div
        style={{
          backgroundColor: '#2ecc71',
          color: 'white',
          padding: '2rem',
          borderRadius: '10px',
          textAlign: 'center',
          boxShadow: '0px 4px 6px rgba(0, 0, 0, 0.1)',
          width: '80%',
          maxWidth: '400px',
        }}
      >
        <h1
          style={{
            fontSize: '2.5rem',
            fontWeight: 'bold',
            margin: 0,
          }}
        >
          {winner.username}
        </h1>
      </div>
      <div>
        <BackButton />
      </div>
    </div>
  );
}
