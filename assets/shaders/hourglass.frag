#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float u_time;
uniform vec4 u_color1;
uniform vec4 u_color2;
uniform vec4 u_color3;
uniform vec4 u_color4;

varying vec4 v_color;
varying vec2 v_texCoord;

void main() {
    float margin = .01;
    vec4 tex = texture2D(u_texture, v_texCoord);


    vec4 color = vec4(0.);
    vec3 base = vec3(mix(u_color1, u_color2, tex.r));
    vec3 insidebase = vec3(mix(u_color3.rgb, u_color4.rgb, tex.r));

    float time = clamp(u_time, margin, 1. - margin);
    float fill = smoothstep(time - margin, time + margin, tex.b);

    vec3 center = mix(u_color1.rgb, insidebase, fill);
    color.rgb = mix(base, center, tex.g);
//    color.rgb = vec3(tex.g);
//    color.rgb = base;
    color.a = tex.a;
    gl_FragColor = color;
}