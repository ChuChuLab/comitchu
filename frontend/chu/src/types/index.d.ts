// TypeScript가 타입을 인식하지 못하는 에셋을 위한 선언

declare module "*.module.css" {
  const classes: { [key: string]: string };
  export default classes;
}

declare module "*.svg" {
  const content: string;
  export default content;
}
