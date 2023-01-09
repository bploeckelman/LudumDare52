#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float u_percent;
uniform vec2 u_dimensions;

varying vec4 v_color;
varying vec2 v_texCoord;
const float border = .5;
const float borderBlend = 3.;

void main() {
    float pixelPercent = u_percent * u_dimensions.x;
    vec2 pixelCoords = v_texCoord * u_dimensions;
    float alphaX = max(smoothstep(border+borderBlend,border, pixelCoords.x), smoothstep(u_dimensions.x - (border+borderBlend), u_dimensions.x -border, pixelCoords.x));
    float alphaY = max(smoothstep(border+borderBlend,border, pixelCoords.y), smoothstep(u_dimensions.y - (border+borderBlend), u_dimensions.y -border, pixelCoords.y));
    float alphaGolf = .5 * smoothstep(pixelPercent + borderBlend, pixelPercent - borderBlend, pixelCoords.x);
    vec4 color = v_color;
//    color.r = smoothstep(border+borderBlend, border, pixelCoords.x);
//    color.g = pixelCoords.x/90.;
    color.a = max(color.a,max(alphaGolf, max(alphaX, alphaY)));
    gl_FragColor = color;
}