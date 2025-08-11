import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import App from "./App";
import "./index.css";
import "./i18n";
import FaultyTerminal from "./components/ReactBits/FaultyTerminal";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <BrowserRouter>
      <div style={{ position: "relative", width: "100%", height: "100vh" }}>
        <FaultyTerminal
          scale={2}
          gridMul={[2, 1]}
          digitSize={1.5}
          timeScale={0.5}
          pause={false}
          scanlineIntensity={0}
          glitchAmount={0}
          flickerAmount={0}
          noiseAmp={1}
          chromaticAberration={0}
          dither={0}
          curvature={0}
          tint="#ffffffff"
          mouseReact={true}
          mouseStrength={1}
          pageLoadAnimation={true}
          brightness={0.2}
        />
      </div>
      <div
        style={{
          position: "absolute",
          top: 0,
          left: 0,
          width: "100%",
          height: "100%",
          zIndex: 1,
          pointerEvents: "none",
        }}
      >
        <App />
      </div>
    </BrowserRouter>
  </React.StrictMode>
);
